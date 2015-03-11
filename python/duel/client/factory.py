import sys, json, datetime, time, random

from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketClientFactory

class DuelClientFactory(WebSocketClientFactory):
    pass

