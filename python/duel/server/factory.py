import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerFactory
from duel.base.game import Game, WAITING_FOR_OPPONENT

from duel.server.bot import DuelBotProtocol
from duel import db
class DuelServerFactory(WebSocketServerFactory):
    """ Server Factory gather all informations about
        connected users and running games toghether.
        
        Since factory have access to all clients, every action related to manage them,
        is the factory responsibility.
        
    """
    
    def __init__(self, url, debug = False, debugCodePaths=False):
        WebSocketServerFactory.__init__(self, url, debug=debug, debugCodePaths=debugCodePaths)
        
        # Initialize data structures
        self.clients = {}
        self.games = {}
        self.cycles = 0
        self.all_questions = {}
        self.last_question_number_added_to_db = -1
        
        # Start mach_making
        self.match_making()
        
    def match_making(self):
        """ Match making loops every second in reactor,
            and make an appropriate opponent to every client that wants to play.
        """
                
        GAMESTATUS = ["WAITING_FOR_OPPONENT", "JOINING", "PREGAME_GAP", "READY_TO_PLAY", "PLAYING", "GAME_END"]
    
        l = r = None
        keys = self.clients.keys()
        for key in keys:
            client = self.clients[key]
            if client.game_data is None or client.game_data.status != WAITING_FOR_OPPONENT:
                continue
            
            print "match_making  ", client.user.name, GAMESTATUS[client.game_data.status]
                
            if l is None:
                l = client
            else:
                if l.game_data.category != client.game_data.category:
                    continue
                r = client
            
            if l is not None and r is None and (datetime.datetime.now() - client.game_data.times['WP']).seconds > 2:
                bot = DuelBotProtocol()
                bot.factory = self
                bot.category = l.game_data.category
                bot.start()
                r = bot
            
            if l and r:
                game = Game()
                self.games[game.hashid] = game
                
                game.join(r)
                game.join(l)
                
                game.prepare(r.game_data.category)
                
                l = r = None
        
        print "-"*50
        
        reactor.callLater(1, self.match_making)
        
        if self.cycles % 10 == 0:
            self.update_questions()
        self.cycles += 1
        
    def register(self, client):
        """ Registering server side client (server protocol) into factory
        """
        if client.user and client.user.user_number:
            self.clients[client.user.user_number] = client
            if self.clients.has_key(client.hashid):
                del self.clients[client.hashid]
        else:
            self.clients[client.hashid] = client

    def unregister(self, client):
        """ Unregistering server side client (server protocol) from factory
        """
        if client.user:
            if self.clients.has_key(client.user.user_number):
                del self.clients[client.user.user_number]
        else:
            if self.clients.has_key(client.hashid):
                del self.clients[client.hashid]
                
    def update_questions(self):
        l_q_n = int(db.info.find_one({'name':'question_number'})['value'])
        for question in db.question.find({'accepted':{'$ne':False}, 'question_number':{'$gt':self.last_question_number_added_to_db}}, {'question_number':1, 'category':1}):
            if question['question_number'] == 1000000000:
                l_q_n += 1
                question['question_number'] = l_q_n
                db.question.update({'_id':question['_id']}, {'$set':{'question_number':l_q_n}})

            cat = str(question['category'])
            q_n = str(question['question_number'])
            
            if not self.all_questions.has_key(cat):
                self.all_questions[cat] = []
            if q_n not in self.all_questions[cat]:
                self.all_questions[cat].append(q_n)
        
        if l_q_n > self.last_question_number_added_to_db:
            db.info.update({'name':'question_number'}, {'$set':{'value':l_q_n}})
            self.last_question_number_added_to_db = l_q_n
        
        
        
        
        
        
        