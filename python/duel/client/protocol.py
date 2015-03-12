import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketClientProtocol
from duel.base.message_handler import MessageHandler

class ClientMessageHandler(MessageHandler):
    def on_login_info(self):
        self.client.user_number = self.payload['user_number']
        print 'logined:', self.client.user_number
        
    def on_your_opponent_is(self):
        print 'Your opponent is', self.payload['opponents'][0]['name']
        self.client.loading()
    
    def on_start_playing(self):
        self.client.sendMessage({'code':'GQ', 'time':-1, 'ok':0})
        
    def on_opponent_score(self):
        time = self.payload['time']
        ok = self.payload['ok']
        print time, ok
        return
        self.whenDoIDisable = time - 1
        
        if self.client.answeredThis == False:
            self.client.sendMessage({'code':'GQ', 'time':-1, 'ok':0})
        
    def on_game_ended(self):
        res = {0:'tie', 1:'win', -1:'lose'}
        print res[self.payload['result']], self.payload['rank'], self.payload['saved_time']
        
    def on_ask_question(self):
        # self.client.question_no += 1
        self.client.answeredThis = False
        self.whenDoIDisable = 0
        self.answer_time = random.randint(19, 20)
        reactor.callLater(1, self.time)
        self.remaining = 20
    
    def on_opponent_has_left(self):
        print 'opponent %s has left the game.'%self.payload['user_number']
        
    def on_friend_logged_in(self):
        print 'friend %s logged in.'%self.payload['user_number']
        
    def on_friend_logged_out(self):
        print 'friend %s logged out.'%self.payload['user_number']
        
    def on_recieve_friend_request(self):
        print self.payload['user_number'], self.payload['name'], self.payload['elo'], self.payload['avatar']
        self.client.send_answer_friend_request(self.payload['user_number'])
        
    def on_receive_friend_list(self):
        print self.payload['friends']
        
    def time(self):
        
        if self.whenDoIDisable > 0:
            return
        
        self.remaining -= 1
        
        if self.whenDoIDisable == self.remaining:
            self.client.sendMessage({'code':'GQ', 'time':-1, 'ok':0})
            return
        
        if self.remaining <= self.answer_time:
            self.client.answeredThis = True
            self.client.sendMessage({'code':'GQ', 'time':self.remaining, 'ok':random.randint(0,1)})
            #self.client.sendMessage({'code':'GQ', 'time':self.remaining, 'ok':1})
            return;
            
        if self.remaining > 0:
            reactor.callLater(1, self.time)
        else:
            self.client.sendMessage({'code':'GQ', 'time':-1, 'ok':0})
            
class DuelClientProtocol(WebSocketClientProtocol):
    """ Handle clinet side of a client connection
    """
    def onOpen(self):
        a = raw_input()
        if a == '':
            self.sendMessage({'code':'RU', 'user_id':'3', 'name':'hesam', 'ostan':'tehran', 'email':'3@3.com', 'avatar':'av3'})
        else:
            self.sendMessage({'code':'UL', 'user_id':a})
        
        self.sendMessage({'code':'WP', 'category':5})
        self.sendMessage({'code':'GFL'})
        self.answeredThis = False
        
    def onPing(self, payload):
        pass
    
    def do_pong(self, payload=None):
        pass
        
    def onMessage(self, payload, isBinary):
        ClientMessageHandler(self, payload, isBinary)
    
    def sendMessage(self, payload, isBinary=False, fragmentSize=None, sync=False, doNotCompress=False):
        payload = json.dumps(payload)
        WebSocketClientProtocol.sendMessage(self, payload, isBinary=isBinary, fragmentSize=fragmentSize, sync=sync, doNotCompress=doNotCompress)

    def send_ready_to_play(self):
        print 'Ready to Play'
        self.sendMessage({'code':'RTP'})
        
    def send_answer_friend_request(self, user_number):
        self.sendMessage({'code':'AFR', 'accepted':True, 'user_number':user_number})
        
    def loading(self):
        rand = random.randint(1,3)
        print 'wait for loading', rand
        time.sleep(rand)
        reactor.callLater(1, self.send_ready_to_play)