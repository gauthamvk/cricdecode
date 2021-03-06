import webapp2
import json
import hashlib
from google.appengine.ext import ndb

class per(ndb.Model):
    bat_balls = ndb.IntegerProperty(indexed=False)
    bat_bowler_type = ndb.StringProperty(indexed=False)
    bat_chances = ndb.IntegerProperty(indexed=False)
    bat_dismissal = ndb.StringProperty(indexed=False)
    bat_fielding_position = ndb.StringProperty(indexed=False)
    bat_num = ndb.IntegerProperty(indexed=False)
    bat_runs = ndb.IntegerProperty(indexed=False)
    bat_time = ndb.IntegerProperty(indexed=False)
    bat_fours = ndb.IntegerProperty(indexed=False)
    bat_sixes = ndb.IntegerProperty(indexed=False)
    bowl_balls = ndb.IntegerProperty(indexed=False)
    bowl_catches_dropped = ndb.IntegerProperty(indexed=False)
    bowl_fours = ndb.IntegerProperty(indexed=False)
    bowl_maidens = ndb.IntegerProperty(indexed=False)
    bowl_no_balls = ndb.IntegerProperty(indexed=False)
    bowl_runs = ndb.IntegerProperty(indexed=False)
    bowl_sixes = ndb.IntegerProperty(indexed=False)
    bowl_spells = ndb.IntegerProperty(indexed=False)
    bowl_wides = ndb.IntegerProperty(indexed=False)
    bowl_wkts_left = ndb.IntegerProperty(indexed=False)
    bowl_wkts_right = ndb.IntegerProperty(indexed=False)
    device_id = ndb.IntegerProperty(indexed=True)
    field_byes = ndb.IntegerProperty(indexed=False)
    field_catches_dropped = ndb.IntegerProperty(indexed=False)
    field_circle_catch = ndb.IntegerProperty(indexed=False)
    field_close_catch = ndb.IntegerProperty(indexed=False)
    field_deep_catch = ndb.IntegerProperty(indexed=False)
    field_misfield = ndb.IntegerProperty(indexed=False)
    field_ro_circle = ndb.IntegerProperty(indexed=False)
    field_ro_deep = ndb.IntegerProperty(indexed=False)
    field_ro_direct_circle = ndb.IntegerProperty(indexed=False)
    field_ro_direct_deep = ndb.IntegerProperty(indexed=False)
    field_slip_catch = ndb.IntegerProperty(indexed=False)
    field_stumpings = ndb.IntegerProperty(indexed=False)
    inning = ndb.IntegerProperty(indexed=True)
    match_id = ndb.IntegerProperty(indexed=True)
    per_id = ndb.IntegerProperty(indexed=False)
    user_id = ndb.StringProperty(indexed=True)

class per_insert(webapp2.RequestHandler):

    def post(self):

        self.response.headers['Content-Type'] = 'text/plain'
        uid = self.request.get('user_id')
        handshake = self.request.get('hSAhnedk')
        times = int(handshake[3])
        handkey = handshake[:3]+handshake[4:]
        key = uid
        for i in xrange(times):
            key = hashlib.md5(key).hexdigest() 

        if(handkey == key):
            per_json = {}
            per_json = json.loads(self.request.get('perData'))
            per_array = per_json["per"]
            
            for per_obj in per_array:
                perf_obj = per()

                perf_obj.bat_balls = per_obj['o']
                perf_obj.bat_bowler_type = per_obj['t']
                perf_obj.bat_chances = per_obj['v']
                perf_obj.bat_dismissal = per_obj['s']
                perf_obj.bat_fielding_position = per_obj['u']
                perf_obj.bat_num = per_obj['m']
                perf_obj.bat_runs = per_obj['n']
                perf_obj.bat_time = per_obj['p']
                perf_obj.bat_fours = per_obj['q']
                perf_obj.bat_sixes = per_obj['r']
                perf_obj.bowl_balls = per_obj['w']
                perf_obj.bowl_catches_dropped = per_obj['a5']
                perf_obj.bowl_fours = per_obj['a1']
                perf_obj.bowl_maidens = per_obj['y']
                perf_obj.bowl_no_balls = per_obj['a6']
                perf_obj.bowl_runs = per_obj['z']
                perf_obj.bowl_sixes = per_obj['a2']
                perf_obj.bowl_spells = per_obj['x']
                perf_obj.bowl_wides = per_obj['a7']
                perf_obj.bowl_wkts_left = per_obj['a3']
                perf_obj.bowl_wkts_right = per_obj['a4']
                perf_obj.device_id = per_obj['did']
                perf_obj.field_byes = per_obj['b7']
                perf_obj.field_catches_dropped = per_obj['b9']
                perf_obj.field_circle_catch = per_obj['a0']
                perf_obj.field_close_catch = per_obj['a9']
                perf_obj.field_deep_catch = per_obj['b1']
                perf_obj.field_misfield = per_obj['b8']
                perf_obj.field_ro_circle = per_obj['b2']
                perf_obj.field_ro_deep = per_obj['b4']
                perf_obj.field_ro_direct_circle = per_obj['b3']
                perf_obj.field_ro_direct_deep = per_obj['b5']
                perf_obj.field_slip_catch = per_obj['a8']
                perf_obj.field_stumpings = per_obj['b6']
                perf_obj.inning = per_obj['l']
                perf_obj.match_id = per_obj['mid']
                perf_obj.per_id = per_obj['pid']
                perf_obj.user_id = per_obj['uid']

                obj_list = per.query(ndb.AND(per.user_id == perf_obj.user_id,per.match_id == perf_obj.match_id,per.device_id == perf_obj.device_id,per.inning == perf_obj.inning)).fetch()
                if(len(obj_list) == 0):
                    perf_obj.put()

            self.response.write('{"status" : 1}')


class per_fetch(webapp2.RequestHandler):

     def post(self):
        self.response.headers['Content-Type'] = 'text/plain'

        user_id = self.request.get("user_id")
        obj_list = per.query(per.user_id == user_id).fetch()

        json_obj = {}
        per_array = []
        for obj in obj_list:
            per_obj = {}

            per_obj["bat_balls"] = obj.bat_balls
            per_obj["bat_bowler_type"] = obj.bat_bowler_type
            per_obj["bat_chances"] = obj.bat_chances
            per_obj["bat_dismissal"] = obj.bat_dismissal
            per_obj["bat_fielding_position"] = obj.bat_fielding_position
            per_obj["bat_num"] = obj.bat_num
            per_obj["bat_runs"] = obj.bat_runs
            per_obj["bat_time"] = obj.bat_time
            per_obj["bat_fours"] = obj.bat_fours
            per_obj["bat_sixes"] = obj.bat_sixes

            per_obj["bowl_balls"] = obj.bowl_balls
            per_obj["bowl_catches_dropped"] = obj.bowl_catches_dropped
            per_obj["bowl_fours"] = obj.bowl_fours
            per_obj["bowl_maidens"] = obj.bowl_maidens
            per_obj["bowl_no_balls"] = obj.bowl_no_balls
            per_obj["bowl_runs"] = obj.bowl_runs
            per_obj["bowl_sixes"] = obj.bowl_sixes
            per_obj["bowl_spells"] = obj.bowl_spells
            per_obj["bowl_wides"] = obj.bowl_wides
            per_obj["bowl_wkts_left"] = obj.bowl_wkts_left
            per_obj["bowl_wkts_right"] = obj.bowl_wkts_right

            per_obj["field_byes"] = obj.field_byes
            per_obj["field_catches_dropped"] = obj.field_catches_dropped
            per_obj["field_circle_catch"] = obj.field_circle_catch
            per_obj["field_close_catch"] = obj.field_close_catch
            per_obj["field_deep_catch"] = obj.field_deep_catch
            per_obj["field_misfield"] = obj.field_misfield
            per_obj["field_ro_circle"] = obj.field_ro_circle
            per_obj["field_ro_deep"] = obj.field_ro_deep
            per_obj["field_ro_direct_circle"] = obj.field_ro_direct_circle
            per_obj["field_ro_direct_deep"] = obj.field_ro_direct_deep
            per_obj["field_slip_catch"] = obj.field_slip_catch
            per_obj["field_stumpings"] = obj.field_stumpings

            per_obj["inning"] = obj.inning
            per_obj["match_id"] = obj.match_id
            per_obj["per_id"] = obj.per_id            
            per_obj["device_id"] = obj.device_id
            per_obj["user_id"] = obj.user_id

            per_array.append(per_obj)


        json_obj["performances"] = per_array
        self.response.write(json.dumps(json_obj))

class per_delete(webapp2.RequestHandler):

     def post(self):
        self.response.headers['Content-Type'] = 'text/plain'
        del_matches = {}
        del_matches = json.loads(self.request.get("del_matches"))
        user_id = del_matches['user_id']
        matches = del_matches['matches']        
        for matc in matches:            
            mid = matc['mid']
            dev = matc['devid']
            ndb.delete_multi(per.query(per.user_id == user_id,per.match_id == mid,per.device_id == dev).fetch(keys_only=True))



application = webapp2.WSGIApplication([
    ('/insert', per_insert), ('/fetch', per_fetch), ('/delete', per_delete)
], debug=True)
