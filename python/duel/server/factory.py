import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketServerFactory
from duel.base.game import Game, WAITING_FOR_OPPONENT

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
                
                game.prepare()
                
                l = r = None
            
        reactor.callLater(1, self.match_making)
        
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
                
