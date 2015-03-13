from pymongo import Connection, ASCENDING
db = Connection().duel

db.user.ensure_index([("user_id" , ASCENDING), ("unique" , True)])
db.user.ensure_index([("user_number" , ASCENDING), ("unique" , True)])
db.question.ensure_index([("question_number" , ASCENDING), ("unique" , True)])
db.question.ensure_index([("category" , ASCENDING)])

for question in db.question.find({'question_number':None}):
    question_number = db.info.find_and_modify({'name':'question_number'}, {'$inc':{'value':1}}, new=True)['value']
    db.question.update({'_id':question['_id']}, {'$set':{'question_number':int(question_number)}})

import server.protocol
import server.factory

import client.protocol
import client.factory
