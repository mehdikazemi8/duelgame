import sys, json, datetime, time, random, operator

from duel import db
NUMBER_OF_PROBLEMS = 6

WAITING_FOR_OPPONENT = 0
JOINING = 1
PREGAME_GAP = 2
READY_TO_PLAY = 3
PLAYING = 4
GAME_END = 5

class GameData(object):
    def __init__(self):
        self.status = WAITING_FOR_OPPONENT
        self.current_step = 0
        self.scores = []
        self.score = 0
        self.saved_time = 0
        self.rank_in_game = None
        self.result_in_game = None
    
    def add_score(self, time, ok):
        self.scores.append({'time':time, 'ok':ok, 'dt':datetime.datetime.now(), 'question_index':self.current_step - 1})
        
        if ok == 1:
            if self.current_step <=5:
                self.score += 3
            else:
                self.score += 5
            self.saved_time += time
        else:
            self.score += -1
        
class Game(object):
    def __init__(self):
        self.participants = {}
        self.category = None
        self.to_ask = []
        self.hashid = id(self)
    
    def join(self, client):
        client.game_data.status = JOINING
        client.game = self
        
        for key, participant in self.participants.iteritems():
            participant.send_your_opponent_is([client])
                
        if len(self.participants.keys()):
            client.send_your_opponent_is(self.participants.values())
        client.game_data.status = PREGAME_GAP
        
        self.participants[client.user.user_number] = client
    
    def left(self, client):
        for key, participant in self.participants.iteritems():
            if key == client.user.user_number:
                continue
            participant.send_opponent_has_left(client)
    
    def prepare(self):
        to_ask_list = []
        allProblems = list(db.question.find({}))
        db_length = len(allProblems)
        
        self.to_ask = []
        while True:
            if len(self.to_ask) >= NUMBER_OF_PROBLEMS:
                break
        
            push_index = random.randint(0, db_length-1)
            if push_index in to_ask_list:
                continue
            else:
                to_ask_list.append(push_index)
            
            thisProblem = { 'question_text':allProblems[push_index]['title'], 
                            'question_id':allProblems[push_index]['_id'],
                            'options':[ allProblems[push_index]['answer'],
                                        allProblems[push_index]['option_two'],
                                        allProblems[push_index]['option_three'],
                                        allProblems[push_index]['option_four']
                                    ],
                            
                        }
            self.to_ask.append(thisProblem)
                
        for key, participant in self.participants.iteritems():
            participant.send_recieve_game_data()

    def start(self):
        for key, participant in self.participants.iteritems():
            if participant.game_data.status != READY_TO_PLAY:
                return

        for key, participant in self.participants.iteritems():
            participant.send_start_playing()
    
    def ask_question(self):
        first_player_step = self.participants.values()[0].game_data.current_step
        
        for key, participant in self.participants.iteritems():
            if participant.game_data.current_step != first_player_step:
                return
            
        for key, participant in self.participants.iteritems():
            participant.send_ask_question()

    def new_score(self, client, time, ok):
        if self.participants[client.user.user_number].game_data.current_step < 1:
            return
        
        self.participants[client.user.user_number].game_data.add_score(time, ok)
        
        for key, participant in self.participants.iteritems():
            if key == client.user.user_number:
                continue
            participant.send_opponent_score(client.user.user_number, time, ok)

    def the_end(self):
        for key, participant in self.participants.iteritems():
            if participant.game_data.status != GAME_END:
                return
        
        rank_list = {}
        for key, participant in self.participants.iteritems():
            rank_list[key] = participant.game_data.score    
        rank_list = sorted(rank_list.items(), key=operator.itemgetter(1), reverse=True)
        
        rank = 1
        for i in range(len(rank_list)):
            if i > 0 and rank_list[i-1][1] != rank_list[i][1]:
                rank += 1
            self.participants[rank_list[i][0]].game_data.rank_in_game = rank
        
        if self.participants[rank_list[0][0]].game_data.rank_in_game == self.participants[rank_list[1][0]].game_data.rank_in_game:
            self.participants[rank_list[0][0]].game_data.result_in_game = 0
            self.participants[rank_list[1][0]].game_data.result_in_game = 0
        else:
            self.participants[rank_list[0][0]].game_data.result_in_game = 1
            self.participants[rank_list[1][0]].game_data.result_in_game = -1
            
        for key, participant in self.participants.iteritems():
            participant.send_the_end(result=participant.game_data.result_in_game, rank=participant.game_data.rank_in_game, saved_time=participant.game_data.saved_time)
        
        self.save()
        self.delete()

    def save(self):
        to_save = []
        for key, participant in self.participants.iteritems():
            for item in participant.game_data.scores:
                item['question_id'] = self.to_ask[item['question_index']]['question_id']
                item['user_number'] = participant.user.user_number
                item['game_id'] = self.hashid
                to_save.append(item)
        db.question_user.insert(to_save)
    
    def delete(self):
        for key, participant in self.participants.iteritems():
            participant.game_data = None
            participant.game = None
        
        try:
            del participant.factory.games[self.hashid]
        except: pass





