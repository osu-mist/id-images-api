import unittest, sys, json, math, operator
from PIL import Image
from api_request import *
from configuration_load import *

class gateway_tests(unittest.TestCase):

    def test_verbs(self):
        self.assertEqual(basic_request
            (url, access_token, "get").status_code, 200)
        self.assertEqual(basic_request
            (url, access_token, "post").status_code, 405)
        self.assertEqual(basic_request
            (url, access_token, "put").status_code, 405)
        self.assertEqual(basic_request
            (url, access_token, "delete").status_code, 405)

    def test_not_found(self):
        bad_url = url + "012345678"
        self.assertEqual(basic_request
            (bad_url, access_token).status_code, 404)

    def test_image(self):
        expected_image = Image.open("images/defaultImage.jpg")
        response_image = get_image(url, access_token)
        
        h1 = expected_image.histogram()
        h2 = response_image.histogram()

        rms = math.sqrt(reduce(operator.add,
                           map(lambda a,b: (a-b)**2, h1, h2))/len(h1))

        self.assertLess(rms, 100)

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

    url = get_request_url(config_path)
    access_token = get_access_token(config_path)

    unittest.main()