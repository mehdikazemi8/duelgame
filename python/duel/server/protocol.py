# -*- coding: utf-8 -*-
import sys, json, datetime, time, random, json, bson, datetime

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerProtocol
from duel.base.message_handler import MessageHandler

from duel import db
from duel.base.user import User
from duel.base.game import *

from duel.base.game import WAITING_FOR_OPPONENT, JOINING, PREGAME_GAP, READY_TO_PLAY, PLAYING, GAME_END

class ServerMessageHandler(MessageHandler):
    def on_register_user(self):
        user = User(user_id=self.payload['user_id'])
        data = self.payload
        del data['code']
        user.save(**data)
        self.client.login(user)
        
    def on_user_login(self):
        user = User(user_id=self.payload['user_id'])
        try: user.user_number
        except: user = None
        self.client.login(user)
    
    def on_user_left_game(self):
        if self.client.game_data.status in [WAITING_FOR_OPPONENT, JOINING, PREGAME_GAP, READY_TO_PLAY]:
            if self.client.game:
                for key, participant in self.client.game.participants.iteritems():
                    if key == self.client.user.user_number:
                        continue
                    participant.game_data.status = WAITING_FOR_OPPONENT
                    participant.game = None
            
                del self.client.factory.games[self.client.game.hashid]
        elif self.client.game_data.status in [PLAYING, GAME_END]:
            if self.client.game:
                self.client.game.left(self.client)
            
        self.client.game = None
        self.client.game_data = None
        
        
    def on_wanna_play(self):
        self.client.game_data = GameData()
        self.client.game_data.times['WP'] = datetime.datetime.now()
        self.client.game_data.category = self.payload['category']
        
    def on_ready_to_play(self):
        if self.client.game_data.status != PREGAME_GAP:
            return
        self.client.game_data.status = READY_TO_PLAY
        self.client.game.start()
    
    def on_get_question(self):
        hint_options = []
        if self.payload.has_key('hint_options'):
            hint_options = self.payload['hint_options']
            
        self.client.game.new_score(self.client, self.payload['time'], self.payload['ok'], hint_options)
        if self.payload['ok'] == 0 and self.payload['time'] >= 0:
            return
        
        self.client.game_data.current_step += 1
        if self.client.game_data.current_step < 6:
            self.client.game.ask_question()
        else:
            self.client.game_data.status = GAME_END
            self.client.game.the_end()
    
    def on_user_challenge(self):
        # self.client.user.user_number challenged target_user_number
        target_user_number = self.payload['user_number']
        category = self.payload['category']
        
        if self.client.factory.clients.has_key(target_user_number):
            #target_user_number is online
            #update target_client
            target_client = self.client.factory.clients[target_user_number]
            target_client.send_challenge_request(self.client.user.user_number, category)
        else:
            #target_user_number is offline
            #update target_user
            pass
            
    def on_add_friend(self):
        # self.client.user.user_number requested friendship to target_user_number
        target_user_number = self.payload['user_number']
        
        #update self.client with new friendship request.
        self.client.user.friends[target_user_number] = {'status':'pending'}
        self.client.user.save(friends=self.client.user.friends)
        
        if self.client.factory.clients.has_key(target_user_number):
            #target_user_number is online
            #update target_client
            target_client = self.client.factory.clients[target_user_number]
            target_client.user.friends[self.client.user.user_number] = {'status':'request'}
            target_client.user.save(friends=target_client.user.friends)
            
            #send to target that you have one new friend request
            target_client.send_receive_friend_request(self.client)
        else:
            #target_user_number is offline
            #update target_user
            target_user = User(user_number=target_user_number)
            target_user.friends[self.client.user.user_number] = {'status':'request'}
            target_user.save(friends=target_user.friends)
        
    def on_answer_friend_request(self):
        # self.client.user.user_number answered friend request of target_user_number
        target_user_number = self.payload['user_number']
        accepted = self.payload['accepted']
        
        #update self.client
        friends = self.client.user.friends
        if accepted:
            friends[target_user_number] = {'status':'friend'}
        else:
            del friends[target_user_number]
        self.client.user.save(friends=friends)
        
        if self.client.factory.clients.has_key(target_user_number):
            #target_user_number is online
            target_client = self.client.factory.clients[target_user_number]
            if accepted:
                target_client.user.friends[self.client.user.user_number] = {'status':'friend'}
            else:
                del target_client.user.friends[self.client.user.user_number]
            target_client.user.save(friends=target_client.user.friends)
        else:
            #target user number is offline
            target_user = User(user_number=target_user_number)
            if accepted:
                target_user.friends[self.client.user.user_number] = {'status':'friend'}
            else:
                del target_user.friends[self.client.user.user_number]
            target_user.save(friends=target_user.friends)
        
    def on_get_friends_list(self):
        self.client.send_receive_friends_list()
    
    def on_user_add_question(self):
        data = {'question_text':self.payload['question_text'],
                'answer':self.payload['answer'],
                'option_two':self.payload['option_two'],
                'option_three':self.payload['option_three'],
                'option_four':self.payload['option_four'],
                'author':self.client.user.user_number,
                'accepted':False,
                }
        db.question.save(data)
        self.client.send_add_question_successful()
    
class DuelServerProtocol(WebSocketServerProtocol):
    """ Handle the server side of a client connection.
    """
    def __init__(self):
        self.is_connected = False
        self.status = None
        
        self.game_data = None
        self.game = None
        self.hashid = None
        self.user = None
        
    def onOpen(self):
        self.is_connected = True
        self.hashid = self.peer.split(':')[2]
        self.factory.register(self)
        print 'Connection %s established.'%self.hashid
        
    def do_ping(self, payload=None):
        pass

    def onPong(self, payload):
        pass
        
    def onMessage(self, payload, isBinary):
        print payload
        ServerMessageHandler(self, payload, isBinary)
    
    def sendMessage(self, payload, isBinary=False, fragmentSize=None, sync=False, doNotCompress=False):
        payload = json.dumps(payload)
        WebSocketServerProtocol.sendMessage(self, payload, isBinary=isBinary, fragmentSize=fragmentSize, sync=sync, doNotCompress=doNotCompress)
        
    def connectionLost(self, reason):
        WebSocketServerProtocol.connectionLost(self, reason)
        if self.game:
            self.game.left(self)
        if self.user and self.user.user_number:
            self.send_friend_logged_out()
        self.factory.unregister(self)

    def login(self, user):
        self.user = user
        self.factory.register(self)
        
        self.send_login_info(user)
        self.send_friend_logged_in()
        print '%s(%s) loginned.'%(self.user.name, self.hashid)
        
    def send_login_info(self, user):
        if user.user_number:
            msg = {'code':'LI', 'name':user.name, 'user_number':user.user_number, 'avatar':user.avatar, 'ostan':user.ostan, 'score':user.score, 'time':user.time, 'elo':user.elo.mu, 'events':[]}
        else:
            msg = {'code':'LI', 'user_number':None}
        self.sendMessage(msg)
        
    def send_your_opponent_is(self, opponents):
        msg = {'code':'YOI', 'opponents':[]}
        
        for opponent in opponents:
            msg['opponents'].append({'name':opponent.user.name, 'avatar':opponent.user.avatar, 'elo':opponent.user.elo.mu, 'user_number':opponent.user.user_number, 'ostan':opponent.user.ostan})
        
        self.sendMessage(msg)
    
    def send_receive_game_data(self):
        msg = {'code':'RGD'}
        
        to_ask = self.game.to_ask
        for i in range(len(to_ask)):
            question_i = to_ask[i].copy()
            for key in ['question_number', 'category']:
                del question_i[key]
            msg['problem' + str(i)] = question_i
            
        self.sendMessage(msg)
        
    def send_start_playing(self):
        msg = {'code':'SP'}
        self.sendMessage(msg)
        
    def send_ask_question(self):
        msg = {'code':'AQ'}
        self.sendMessage(msg)
    
    def send_opponent_score(self, user_number, time, ok):
        msg = {'code':'OS', 'user_number':user_number, 'time':time, 'ok':ok}
        self.sendMessage(msg)
    
    def send_the_end(self, score, result, rank, saved_time, new_elo):
        msg = {'code':'GE', 'result':result, 'rank':rank, 'saved_time':saved_time, 'new_elo':new_elo.mu, 'score':score}
        self.sendMessage(msg)
        
    def send_receive_friend_request(self, client):
        msg = {'code':'RFR', 'user_number': client.user.user_number, 'name':client.user.name, 'elo':client.user.elo.mu, 'avatar':client.user.avatar}
        self.sendMessage(msg)
        
    def send_opponent_has_left(self, client):
        msg = {'code':'OHL', 'user_number': client.user.user_number}
        self.sendMessage(msg)
        
    def send_friend_logged_in(self):
        msg = {'code':'FLI', 'user_number': self.user.user_number, 'name':self.user.name, 'elo':self.user.elo.mu, 'avatar':self.user.avatar}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            if friend_data['status'] != 'friend':
                continue
            if self.factory.clients.has_key(friend_user_number):
                friend_client = self.factory.clients[friend_user_number]
                friend_client.sendMessage(msg)
                
    def send_friend_logged_out(self):
        msg = {'code':'FLO', 'user_number': self.user.user_number, 'name':self.user.name, 'elo':self.user.elo.mu, 'avatar':self.user.avatar}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            if friend_data['status'] != 'friend':
                continue
            if self.factory.clients.has_key(friend_user_number):
                friend_client = self.factory.clients[friend_user_number]
                friend_client.sendMessage(msg)
        
    def send_receive_friends_list(self):
        msg = {'code':'RFL', 'friends': {}}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            msg['friends'][friend_user_number] = friend_data
            if self.factory.clients.has_key(friend_user_number):
                msg['friends'][friend_user_number]['is_online'] = True
            else:
                msg['friends'][friend_user_number]['is_online'] = False
                
        self.sendMessage(msg)
        
    def send_challenge_request(self, user, category):
        msg = {'code':'CR', 'user_number':user.user_number, 'name':user.name, 'avatar':user.avatar}
        self.sendMessage(msg)
        
    def send_add_question_successful(self):
        msg = {'code':'AQS'}
        self.sendMessage(msg)
    
        
        
        