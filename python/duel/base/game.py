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
        self.hint_options = []
        self.hint_options_cost = 0
        self.times = {}
    
    def add_score(self, time, ok, hint_options):
        self.scores.append({'time':time, 'ok':ok, 'dt':datetime.datetime.now(), 'question_index':self.current_step - 1})
                
        if ok == 1:
            if time >= 10:
                if self.current_step < 5:
                    self.score += 3
                else:
                    self.score += 5
            else:
                self.score += 1
                
            self.saved_time += time
        else:
            pass#self.score += -1
        
class Game(object):
    def __init__(self):
        self.participants = {}
        self.category = None
        self.to_ask = []
        self.hashid = id(self)
        self.game_cost = 120
    
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
    
        del self.participants[client.user.user_number]
        
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
        res = [int(item[0]) for item in res[:10]]
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
            participant.game_data.status = PLAYING
            participant.send_start_playing()
            
        for key, participant in self.participants.iteritems():
            participant.user.save(time=participant.user.time - self.game_cost)
    
    def ask_question(self):
        first_player_step = self.participants.values()[0].game_data.current_step
        
        for key, participant in self.participants.iteritems():
            if participant.game_data.current_step != first_player_step:
                return
        
        for key, participant in self.participants.iteritems():
            participant.send_ask_question()

    def new_score(self, client, time, ok, hint_options):
        # is_first = True
        # opponent = None
        # for key, participant in self.participants.iteritems():
            # if key == client.user.user_number:
                # continue
            # opponent = participant
            # break
        
        # for item in opponent.game_data.scores:
            # if item['question_index'] != client.game_data.current_step:
                # continue
            # if item['ok'] == 1:
                # is_first = False
        
        self.participants[client.user.user_number].game_data.add_score(time, ok, hint_options)
        
        for key, participant in self.participants.iteritems():
            if key == client.user.user_number:
                continue
            participant.send_opponent_score(client.user.user_number, time, ok)

    def the_end(self):
        for key, participant in self.participants.iteritems():
            if participant.game_data.status != GAME_END:
                return
        
        #make rank list
        rank_list = {}
        for key, participant in self.participants.iteritems():
            if not rank_list.has_key(participant.game_data.score):
                rank_list[participant.game_data.score] = []
            rank_list[participant.game_data.score].append(key)
        rank_list = sorted(rank_list.items(), key=operator.itemgetter(0), reverse=True)
        
        #make rank for participants
        for key, participant in self.participants.iteritems():
            for i in range(len(rank_list)):
                if key in rank_list[i][1]:
                    participant.game_data.rank_in_game = i + 1
            
        p_a = self.participants.values()[0]
        if len(self.participants.values) > 1:
            p_b = self.participants.values()[1]
            if p_a.game_data.rank_in_game > p_b.game_data.rank_in_game:
                p_a, p_b = p_b, p_a
                
            if p_a.game_data.rank_in_game == p_b.game_data.rank_in_game:
                p_a.game_data.result_in_game = 0
                p_b.game_data.result_in_game = 0
            else:
                p_a.game_data.result_in_game = 1
                p_b.game_data.result_in_game = -1
            
            p_a.user.elo, p_b.user.elo = rate_1vs1(p_a.user.elo, p_b.user.elo, drawn=p_a.game_data.rank_in_game==p_b.game_data.rank_in_game) 
        else:
            p_a.game_data.result_in_game = 1
            
        for key, participant in self.participants.iteritems():
            if participant.game_data.result_in_game == -1:
                participant.game_data.saved_time = 0
            participant.send_the_end(score=participant.game_data.score, result=participant.game_data.result_in_game, rank=participant.game_data.rank_in_game, saved_time=participant.game_data.saved_time, new_elo=participant.user.elo)
        
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
                participant.user.time += self.game_cost + participant.game_data.saved_time
            elif participant.game_data.result_in_game == -1:
                participant.user.statistics['lose'] += 1
            elif participant.game_data.result_in_game == 0:
                participant.user.statistics['draw'] += 1
                participant.user.time += participant.game_data.saved_time
            
            participant.user.time -= participant.game_data.hint_options_cost
            participant.user.score += participant.game_data.score
            participant.user.save()
        
        db.game_log.save({'game_id':self.hashid, 'participants':participants_user_number, 'winner':winners, 'dt':datetime.datetime.now()})
        
        to_save = []
        for key, participant in self.participants.iteritems():
            for item in participant.game_data.scores:
                item['question_number'] = self.to_ask[item['question_index']]['question_number']
                item['category'] = self.to_ask[item['question_index']]['category']
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
        
        del participant.factory.games[self.hashid]
        
        
        