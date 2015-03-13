import sys, json, datetime, time, random, operator
from random import shuffle
from duel import db
from trueskill import rate_1vs1
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
    
    def prepare(self, category):
        cat = str(category)
        for key, participant in self.participants.iteritems():
            if participant.user.seen_data:
                continue
            seen_data = db.seen_data.find_one({'user_number':participant.user.user_number})
            if seen_data is None:
                seen_data = {}
            participant.user.seen_data = seen_data
        
        all_cat_questions = participant.factory.all_questions[cat]
        res = {}
        for q_n in all_cat_questions:
            values = []
            for key, participant in self.participants.iteritems():
                if participant.user.seen_data.has_key(cat) and participant.user.seen_data[cat].has_key(q_n):
                    values.append(participant.user.seen_data[cat][q_n])
                else:
                    values.append(0)
            res[q_n] = (abs(values[0]-values[1]), sum(values))
        
        res = sorted(res.items(), key=operator.itemgetter(1, 0))
        res = [int(item[0]) for item in res[:12]]
        res = sorted(res, key=lambda k: random.random())[:6]
        
        for question in db.question.find({'question_number': { '$in': res}}):
            thisProblem = { 'question_text':question['title'], 
                            'question_number':question['question_number'],
                            'category':question['category'],
                            'options':[ question['answer'],
                                        question['option_two'],
                                        question['option_three'],
                                        question['option_four']
                                    ],
                        }
            self.to_ask.append(thisProblem)
            
        for key, participant in self.participants.iteritems():
            participant.send_receive_game_data()

    def start(self):
        for key, participant in self.participants.iteritems():
            if participant.game_data.status != READY_TO_PLAY:
                return

        for key, participant in self.participants.iteritems():
            participant.send_start_playing()
            
        for key, participant in self.participants.iteritems():
            participant.user.save(time=participant.user.time-120)
    
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
        
        p_a = self.participants[rank_list[0][0]]
        p_b = self.participants[rank_list[1][0]]
        
        is_draw = False
        if p_a.game_data.rank_in_game == p_b.game_data.rank_in_game:
            p_a.game_data.result_in_game = 0
            p_b.game_data.result_in_game = 0
            is_draw = True
        else:
            p_a.game_data.result_in_game = 1
            p_b.game_data.result_in_game = -1
        
        p_a.user.elo, p_b.user.elo = rate_1vs1(p_a.user.elo, p_b.user.elo, drawn=is_draw) 
         
        for key, participant in self.participants.iteritems():
            participant.send_the_end(result=participant.game_data.result_in_game, rank=participant.game_data.rank_in_game, saved_time=participant.game_data.saved_time, new_elo=participant.user.elo)
        
        self.save()
        self.delete()

    def save(self):
        participants_user_number = []
        winners = []
        for key, participant in self.participants.iteritems():
            participants_user_number.append(key)
            if participant.game_data.result_in_game == 1:
                winners.append(key)
                participant.user.statistics['win'] += 1
                participant.user.time += 120 + participant.game_data.saved_time
            elif participant.game_data.result_in_game == -1:
                participant.user.statistics['lose'] += 1
            elif participant.game_data.result_in_game == 0:
                participant.user.statistics['draw'] += 1
                participant.user.time += participant.game_data.saved_time
            participant.user.score += participant.game_data.score
            participant.user.save()
        db.game_log.save({'game_id':self.hashid, 'participants':participants_user_number, 'winner':winners, 'dt':datetime.datetime.now()})
        
        to_save = []
        for key, participant in self.participants.iteritems():
            for item in participant.game_data.scores:
                item['question_number'] = self.to_ask[item['question_index']]['question_number']
                item['user_number'] = participant.user.user_number
                item['game_id'] = self.hashid
                to_save.append(item)
        db.question_log.insert(to_save)
        
        query = {}
        for question in self.to_ask:
            query['%s.%s'%(str(question['category']), str(question['question_number']))] = 1
        
        for key, participant in self.participants.iteritems():
            db.seen_data.save({'user_number':participant.user.user_number}, {'$inc':query}, True)
            
            if participant.user.seen_data:
                for question in self.to_ask:
                    cat = str(question['category'])
                    q_n = str(question['question_number'])
                    if not participant.user.seen_data.has_key(cat):
                        participant.user.seen_data[cat] = {}
                    if not participant.user.seen_data[cat].has_key(q_n):
                        participant.user.seen_data[cat][q_n] = 0
                    participant.user.seen_data[cat][q_n] += 1
    
    def delete(self):
        for key, participant in self.participants.iteritems():
            participant.game_data = None
            participant.game = None
        
        try:
            del participant.factory.games[self.hashid]
        except: pass
        
        
        