from duel import db

def start():
    for item in db.log.find():
        print item
    