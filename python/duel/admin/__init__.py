import web, json, bson, datetime
from pymongo import Connection

db = Connection().duel

### Url mappings
urls = (
    '/', 'Index',
    '/save', 'Save',
    '/delete', 'Delete',
    '/edit', 'Edit'
)


### Templates
render = web.template.render('duel/admin/templates', base='base')


class Index:
    def GET(self):
        questions = db.question.find().sort([('dt', -1)])
        return render.index(questions)

class Save:
    def GET(self):
        _input = web.input()
        dt = datetime.datetime.now()
        item_id = _input['item_id'].strip()
        
        category = int(_input['category'])
        title = _input['title'].strip()
        answer = _input['answer'].strip()
        option_two = _input['option_two'].strip()
        option_three = _input['option_three'].strip()
        option_four = _input['option_four'].strip()
        
        if title == '' or answer == '' or option_two == '' or option_three == '' or option_four == '' :
            return json.dumps({'msg':'NOKEY'})
        
        if answer == option_two or answer == option_three or answer == option_four:
            return json.dumps({'msg':'NOKEY'})
        if option_two == option_three or option_two == option_four:
            return json.dumps({'msg':'NOKEY'})
        if option_three == option_four:
            return json.dumps({'msg':'NOKEY'})
        
        data = {'dt':dt,
                'title':title,
                'option_two':option_two,
                'option_three':option_three,
                'option_four':option_four,
                'category':category,
                'answer':answer}
        if item_id != '':
            db.question.update({'_id':bson.ObjectId(item_id)}, data)
            _id = item_id
        else:
            _id = db.question.save(data)
        return json.dumps({'_id':str(_id), 'msg':'OK'})

class Delete:
    def GET(self):
        _id = web.input()['_id']
        db.question.remove({'_id':bson.ObjectId(_id)})
        return json.dumps({'msg':'OK'})
    
class Edit:
    def GET(self):
        _id = web.input()['_id']
        d = db.question.find_one({'_id':bson.ObjectId(_id)})
        d['_id'] = str(d['_id'])
        d['dt'] = str(d['dt'])
        return json.dumps({'data':d})

class MyApplication(web.application):
    def run(self, port=8080, *middleware):
        func = self.wsgifunc(*middleware)
        return web.httpserver.runsimple(func, ('0.0.0.0', port))

def start():
    app = MyApplication(urls, globals())
    app.run(port=8888)
    
    