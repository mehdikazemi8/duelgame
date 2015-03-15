from pymongo import Connection, ASCENDING
db = Connection().duel

db.user.ensure_index([("user_id" , ASCENDING), ("unique" , True)])
db.user.ensure_index([("user_number" , ASCENDING), ("unique" , True)])
db.question.ensure_index([("question_number" , ASCENDING), ("unique" , True)])
db.question.ensure_index([("category" , ASCENDING)])

import server.protocol
import server.factory

import client.protocol
import client.factory
