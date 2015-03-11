from pymongo import Connection
db = Connection().duel

import server.protocol
import server.factory

import client.protocol
import client.factory
