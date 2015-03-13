import sys, json, datetime, time, random, bson, string
from duel import db
from trueskill import Rating

class RegisterUserException(Exception):
    pass

class User(object):
    def __init__(self, **kwargs):
        """ User Object. fetch a user with _id or user_number or user_id from db.
        """
        self.user_number = None
        self.user_id = None
        self.avatar = 'av1'
        self.name = ''
        self.email = ''
        self.ostan = ''
        self.elo = Rating(1400)
        self.time = 10 * 60
        self.score = 0
        self.friends = {}
        self.seen_data = None
        self.statistics = {'win':0, 'draw':0, 'lose':0}
        
        if not len(kwargs.keys()):
            return
        
        q = {}
        if kwargs.has_key('_id'):
            if type(kwargs['_id']) == str:
                q['_id'] = bson.ObjectId(kwargs['_id'])
            else:
                q['_id'] = kwargs['_id']
    
        elif kwargs.has_key('user_number'):
            q['user_number'] = kwargs['user_number']
        elif kwargs.has_key('user_id'):
            q['user_id'] = kwargs['user_id']
        
        self.load(db.user.find_one(q))
     
    def load(self, data):
        if data is None:
            return
        if data.has_key('user_number'):
            self.user_number = data['user_number']
        if data.has_key('user_id'):
            self.user_id = data['user_id']
        if data.has_key('avatar'):
            self.avatar = data['avatar']
        if data.has_key('name'):
            self.name = data['name']
        if data.has_key('email'):
            self.email = data['email']
        if data.has_key('ostan'):
            self.ostan = data['ostan']
        if data.has_key('elo'):
            if isinstance(Rating, type(data['elo'])):
                self.elo = data['elo']
            elif data['elo']:
                self.elo = Rating(mu=float(data['elo'][0]), sigma=float(data['elo'][1]))
        if data.has_key('score'):
            self.score = data['score']
        if data.has_key('time'):
            self.time = data['time']
        if data.has_key('friends'):
            self.friends = data['friends']
        if data.has_key('statistics'):
            self.statistics = data['statistics']
    
    def to_dict(self):
        return {
            'user_id':self.user_id,
            'name':self.name,
            'avatar':self.avatar,
            'email':self.email,
            'user_number':self.user_number,
            'ostan':self.ostan,
            'elo':(self.elo.mu, self.elo.sigma),
            'score':self.score,
            'time':self.time,
            'friends':self.friends,
            'statistics':self.statistics
        }
        
    def create(self, **kwargs):
        user_number = 1000000 + db.info.find_and_modify({'name':'user_number'}, {'$inc':{'value':1}}, new=True)['value']
        self.user_number = self.int2base(int(user_number), 16)
        
        if not kwargs.has_key('user_id') or not kwargs.has_key('name'):
            raise RegisterUserException('user_id or name are not specified.')

        self.load(kwargs)
        db.user.update({'user_id':self.user_id}, {'$set':self.to_dict()}, True)

    def update(self, **kwargs):
        if len(kwargs.keys()):
            db.user.update({'user_number':self.user_number}, {'$set':dict(kwargs)})
            self.load(kwargs)
        else:
            db.user.update({'user_number':self.user_number}, {'$set':self.to_dict()})
        
    def int2base(self, x, base):
        digs = string.digits + string.letters
        
        digits = []
        
        while x:
            digits.append(digs[x % base])
            x /= base
        
        digits.reverse()
        return ''.join(digits)
    
    
    
    
    
    