provision-localstack:
	@docker-compose up \
		--detach \
		--force-recreate \
		--renew-anon-volumes \
		--remove-orphans

destroy-localstack:
	@docker-compose down \
		--remove-orphans \
		--volumes

show-localstack-logs:
	@docker-compose logs --follow
