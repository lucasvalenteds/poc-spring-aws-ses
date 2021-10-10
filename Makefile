API_URL = http://localhost:8080

EMAIL := john@foocompany.com
PASSWORD = 123456

create-user:
	@curl POST \
		--verbose \
		--header 'Accept: application/json' \
		--header 'Content-Type: application/json' \
		--data '{"email":"$(EMAIL)","password":"$(PASSWORD)","firstname": "John","lastname":"Smith"}' \
		'$(API_URL)/users/create' | jq

delete-user:
	@curl POST \
		--verbose \
		--header 'Accept: application/json' \
		--header 'Content-Type: application/json' \
		--data '{"email":"$(EMAIL)","password":"$(PASSWORD)"}' \
		'$(API_URL)/users/delete' | jq
