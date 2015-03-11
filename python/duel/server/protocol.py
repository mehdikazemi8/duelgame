# -*- coding: utf-8 -*-
import sys, json, datetime, time, random, json, bson, datetime

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerProtocol
from duel.base.message_handler import MessageHandler

from duel import db
from duel.base.user import User
from duel.base.game import *

class ServerMessageHandler(MessageHandler):
    def on_register_user(self):
        user = User()
        user.create(**self.payload)
        self.client.login(user)
        
    def on_user_login(self):
        user = User(user_id=self.payload['user_id'])
        try: user.user_number
        except: user = None
        self.client.login(user)
    
    def on_wanna_play(self):
        self.client.game_data = GameData()
        self.client.game_data.category = self.payload['category']
        
    def on_ready_to_play(self):
        self.client.game_data.status = READY_TO_PLAY
        self.client.game.start()
    
    def on_get_question(self):
        self.client.game.new_score(self.client, self.payload['time'], self.payload['ok'])
        
        self.client.game_data.current_step += 1
        if self.client.game_data.current_step <= 6:
            self.client.game.ask_question()
        else:
            self.client.game_data.status = GAME_END
            self.client.game.the_end()
       
    def on_add_friend(self):
        # self.client.user.user_number requested friendship to target_user_number
        target_user_number = self.payload['user_number']
        
        #update self.client with new friendship request.
        self.client.user.friends[target_user_number] = {'status':'pending'}
        self.client.user.update(friends=self.client.user.friends)
        
        if self.client.factory.clients.has_key(target_user_number):
            #target_user_number is online
            #update target_client
            target_client = self.client.factory.clients[target_user_number]
            target_client.user.friends[self.client.user.user_number] = {'status':'request'}
            target_client.user.update(friends=target_client.user.friends)
            
            #send to target that you have one new friend request
            target_client.send_recieve_friend_request(self.client)
        else:
            #target_user_number is offline
            #update target_user
            target_user = User(user_number=target_user_number)
            target_user.friends[self.client.user.user_number] = {'status':'request'}
            target_user.update(friends=target_user.friends)
        
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
        self.client.user.update(friends=friends)
        
        if self.client.factory.clients.has_key(target_user_number):
            #target_user_number is online
            target_client = self.client.factory.clients[target_user_number]
            if accepted:
                target_client.user.friends[self.client.user.user_number] = {'status':'friend'}
            else:
                del target_client.user.friends[self.client.user.user_number]
            target_client.user.update(friends=target_client.user.friends)
        else:
            #target user number is offline
            target_user = User(user_number=target_user_number)
            if accepted:
                target_user.friends[self.client.user.user_number] = {'status':'friend'}
            else:
                del target_user.friends[self.client.user.user_number]
            target_user.update(friends=target_user.friends)
        
    def on_get_friends_list(self):
        self.client.send_recieve_friends_list()
    
class DuelServerProtocol(WebSocketServerProtocol):
    """ Handle the server side of a client connection.
    """
    def onOpen(self):
        self.is_connected = True
        self.status = None
        
        self.game_data = None
        self.game = None
        self.hashid = self.peer
        self.user = None
        
        self.factory.register(self)
        
    def do_ping(self, payload=None):
        pass

    def onPong(self, payload):
        pass
        
    def onMessage(self, payload, isBinary):
        ServerMessageHandler(self, payload, isBinary)
    
    def sendMessage(self, payload, isBinary=False, fragmentSize=None, sync=False, doNotCompress=False):
        payload = json.dumps(payload)
        WebSocketServerProtocol.sendMessage(self, payload, isBinary=isBinary, fragmentSize=fragmentSize, sync=sync, doNotCompress=doNotCompress)
        
    def connectionLost(self, reason):
        WebSocketServerProtocol.connectionLost(self, reason)
        if self.game:
            self.game.left(self)
        self.send_friend_is_out()
        self.factory.unregister(self)

    def login(self, user):
        self.user = user
        self.factory.register(self)
        
        self.send_login_info(user)
        self.send_friend_is_in()
        
    def send_login_info(self, user):
        if user:
            msg = {'code':'LI', 'user_number':user.user_number, 'score':user.score, 'time':user.time, 'elo':user.elo, 'events':[]}
        else:
            msg = {'code':'LI', 'user_number':None}
        self.sendMessage(msg)
        
    def send_your_opponent_is(self, opponents):
        msg = {'code':'YOI', 'oppData':[]}
        
        for opponent in opponents:
            msg['oppData'].append({'name':opponent.user.name, 'avatar':opponent.user.avatar, 'user_number':opponent.user.user_number})
        
        self.sendMessage(msg)
    
    def send_recieve_game_data(self):
        msg = {'code':'RGD'}
        
        to_ask = self.game.to_ask
        for i in range(len(to_ask)):
            question_i = to_ask[i].copy()
            del question_i['question_id']
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
    
    def send_the_end(self, result, rank, saved_time):
        msg = {'code':'GE', 'result':result, 'rank':rank, 'saved_time':saved_time}
        self.sendMessage(msg)
        
    def send_recieve_friend_request(self, client):
        msg = {'code':'FR', 'user_number': client.user.user_number, 'name':client.user.name, 'elo':client.user.elo, 'avatar':client.user.avatar}
        self.sendMessage(msg)
        
    def send_opponent_has_left(self, client):
        msg = {'code':'OHL', 'user_number': client.user.user_number}
        self.sendMessage(msg)
        
    def send_friend_is_in(self):
        msg = {'code':'FII', 'user_number': self.user.user_number, 'name':self.user.name, 'elo':self.user.elo, 'avatar':self.user.avatar}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            if friend_data['status'] != 'friend':
                continue
            if self.factory.clients.has_key(friend_user_number):
                friend_client = self.factory.clients[friend_user_number]
                friend_client.sendMessage(msg)
                
    def send_friend_is_out(self):
        msg = {'code':'FIO', 'user_number': self.user.user_number, 'name':self.user.name, 'elo':self.user.elo, 'avatar':self.user.avatar}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            if friend_data['status'] != 'friend':
                continue
            if self.factory.clients.has_key(friend_user_number):
                friend_client = self.factory.clients[friend_user_number]
                friend_client.sendMessage(msg)
        
        
    def send_recieve_friends_list(self):
        msg = {'code':'RFL', 'friends': {}}
        for friend_user_number, friend_data in self.user.friends.iteritems():
            msg['friends'][friend_user_number] = friend_data
            if self.factory.clients.has_key(friend_user_number):
                msg['friends'][friend_user_number]['is_online'] = True
            else:
                msg['friends'][friend_user_number]['is_online'] = False
                
        self.sendMessage(msg)
        
        
        
        
        