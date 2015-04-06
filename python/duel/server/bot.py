import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerProtocol
from duel.base.message_handler import MessageHandler

from duel.base.user import User
from duel.base.game import *

class BotMessageHandler(MessageHandler):
    def on_your_opponent_is(self):
        pass#print 'Your opponent is', self.payload['opponents'][0]['name']

    def on_wanna_play(self):
        self.client.game_data = GameData()
        self.client.game_data.times['WP'] = datetime.datetime.now()
        self.client.game_data.category = self.payload['category']
        self.client.game_data.is_bot = True

    def on_receive_game_data(self):
        msg = {'code':'RTP'}
        self.client.sendMessage(msg)

    def on_ready_to_play(self):
        if self.client.game_data.status != PREGAME_GAP:
            return
        self.client.game_data.status = READY_TO_PLAY
        self.client.game.start()

    def on_start_playing(self):
        self.client.answer()

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
    def on_opponent_score(self):
        time = self.payload['time']
        ok = self.payload['ok']

    def on_game_ended(self):
        res = {0:'tie', 1:'win', -1:'lose'}
        print res[self.payload['result']], self.payload['rank'], self.payload['saved_time']

    def on_ask_question(self):
        self.client.answer()

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

class DuelBotProtocol(WebSocketServerProtocol):
    """ Handle bot
    """

    def __init__(self):
        self.is_connected = False
        self.status = None

        self.game_data = None
        self.game = None
        self.hashid = None
        self.user = None

        self.timer = 20

    def start(self):
        self.is_connected = True
        self.hashid = 'bot1'
        self.factory.register(self)

        self.user = User(user_id='bot1')
        try: self.user.user_number
        except: self.user = None

        self.sendMessage({'code':'WP', 'category':self.category})
        print 'BOT Connection established.'

    def onPing(self, payload):
        pass

    def do_pong(self, payload=None):
        pass

    def sendMessage(self, payload, isBinary=False, fragmentSize=None, sync=False, doNotCompress=False):
        payload = json.dumps(payload)
        BotMessageHandler(self, payload, isBinary)

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

    def answer(self):
        self.time = 20 - random.randint(1, 6)
        self.timer = 20
        self.answered_this = False
        reactor.callLater(1, self.countdown)

    def countdown(self):
        if self.answered_this:
            return
        self.timer -= 1

        if self.timer == self.time:
            self.answered_this = random.randint(0, 1)
            self.sendMessage({'code':'GQ', 'time':self.time, 'ok':self.answered_this})

        if self.timer < 0:
            self.sendMessage({'code':'GQ', 'time':-1, 'ok':0})
            return

        reactor.callLater(1, self.countdown)
