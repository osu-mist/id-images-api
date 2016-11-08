import unittest, sys, json, math, operator, requests
from PIL import Image
from api_request import *

class gateway_tests(unittest.TestCase):

    def test_verbs(self):
        self.assertEqual(basic_request
            (request_url, access_token, "get").status_code, 200)
        self.assertEqual(basic_request
            (request_url, access_token, "post").status_code, 405)
        self.assertEqual(basic_request
            (request_url, access_token, "put").status_code, 405)
        self.assertEqual(basic_request
            (request_url, access_token, "delete").status_code, 405)

    def test_not_found(self):
        bad_url = url + "012345678"

        self.assertEqual(basic_request
            (bad_url, access_token).status_code, 404)

    def test_default_image(self):
        default_image_url = url + config_json["osu_id_no_image"]

        expected_image = Image.open("images/defaultImage.jpg")
        response_image = get_image(default_image_url, access_token)
        
        h1 = expected_image.histogram()
        h2 = response_image.histogram()

        rms = math.sqrt(reduce(operator.add,
                           map(lambda a,b: (a-b)**2, h1, h2))/len(h1))

        self.assertLess(rms, 100)

    def test_image_resize(self):
        original_image = get_image(request_url, access_token)

        resize_width = 150
        resize_url = request_url + '?w=' + str(resize_width)
        resized_image = get_image(resize_url, access_token)

        expected_height = ((original_image.height * resize_width) / original_image.width)

        self.assertEqual(expected_height, resized_image.height)
        self.assertEqual(resize_width, resized_image.width)

if __name__ == '__main__':
    options_tpl = ('-i', 'config_path')
    del_list = []
    
    for i,config_path in enumerate(sys.argv):
        if config_path in options_tpl:
            del_list.append(i)
            del_list.append(i+1)

    del_list.reverse()

    for i in del_list:
        del sys.argv[i]

    config_data_file = open(config_path)
    config_json = json.load(config_data_file)

    url = config_json["hostname"] + config_json["version"] + config_json["api"]
    request_url = url + config_json["osu_id_with_image"]

    # Get Access Token
    access_token_url = url + config_json["token_endpoint"]
    post_data = {'client_id': config_json["client_id"],
         'client_secret': config_json["client_secret"],
         'grant_type': 'client_credentials'}
    request = requests.post(access_token_url, data=post_data)
    response = request.json()
    access_token = 'Bearer ' + response["access_token"]

    unittest.main()