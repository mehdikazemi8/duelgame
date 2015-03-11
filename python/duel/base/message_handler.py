import json

class MessageHandler(object):
    def __init__(self, client, payload, is_binary):
        self.client = client
        self.payload = json.loads(payload)
        self.is_binary = is_binary
        
        self.functions = {}
        
        self.load_funcs()
        self.call_funcs()
        
    def load_funcs(self):
        for k, v in self.__class__.__dict__.iteritems():
            if k.startswith('on_'):
                p = k.replace('on_', '').split('_')
                key = ''
                for item in p:
                    key += item[0].upper()
                
                self.functions[key] = v
                
    def call_funcs(self):
        if self.payload['code'] in self.functions.keys():
            self.functions[self.payload['code']](self)
        