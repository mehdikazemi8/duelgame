import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerFactory
from duel.base.game import Game, WAITING_FOR_OPPONENT

from duel import db
class DuelServerFactory(WebSocketServerFactory):
    """ Server Factory gather all informations about
        connected users and running games toghether.
        
        Since factory have access to all clients, every action related to manage them,
        is the factory responsibility.
        
    """
    
    def __init__(self, url, debug = False, debugCodePaths = False):
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
        """ Mach making loops every second in reactor,
            and make an appropriate opponent to every client that wants to play.
        """
        
        l = r = None
        for hashid, client in self.clients.iteritems():
            if client.game_data is None or client.game_data.status != WAITING_FOR_OPPONENT:
                continue
            
            if l is None:
                l = client
            else:
                r = client
            
            if l and r:
                game = Game()
                self.games[game.hashid] = game
                
                game.join(r)
                game.join(l)
                
                game.prepare(r.game_data.category)
                
                l = r = None
        
        reactor.callLater(1, self.match_making)
        
        if self.cycles % 10 == 0:
            self.update_questions()
        self.cycles += 1
        
    def register(self, client):
        """ Registering server side client (server protocol) into factory
        """
        if client.user:
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
        if l_q_n <= self.last_question_number_added_to_db:
            return
        for question in db.question.find({'accepted':{'$ne':False}, 'question_number':{'$gt':self.last_question_number_added_to_db}}, {'question_number':1, 'category':1}):
            cat = str(question['category'])
            q_n = str(question['question_number'])
            if not self.all_questions.has_key(cat):
                self.all_questions[cat] = []
            if q_n not in self.all_questions[cat]:
                self.all_questions[cat].append(q_n)
        self.last_question_number_added_to_db = l_q_n
        
        
        
        
        
        
        
        