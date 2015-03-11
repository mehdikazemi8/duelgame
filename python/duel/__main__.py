import sys, json, os

from twisted.internet import reactor
from twisted.web.server import Site
from twisted.web.static import File

from autobahn.twisted.websocket import listenWS, connectWS, WebSocketClientFactory

from duel.server import DuelServerFactory, DuelServerProtocol
from duel.client import DuelClientFactory, DuelClientProtocol

if __name__ == '__main__':
   if len(sys.argv) > 1:
      if sys.argv[1] == 'server':
         
         FACTORY = DuelServerFactory("ws://localhost:9000",
                                 debug = True,
                                 debugCodePaths = True)
      
         FACTORY.protocol = DuelServerProtocol
         FACTORY.setProtocolOptions(allowHixie76 = True)
         
         listenWS(FACTORY)
         reactor.run()

      elif sys.argv[1] == 'client':
         client_factory = DuelClientFactory('ws://localhost:9000')
         client_factory.protocol = DuelClientProtocol
         connectWS(client_factory)
      
         reactor.run()
      elif sys.argv[1] == 'admin':
         from duel.admin import start
         #os.system('gnome-terminal -e "sudo"')

         start()