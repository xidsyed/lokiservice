# lokiservice
a mutltiplatoform app with geocoding, geolocation, autocomplete service 'loki' . for testing purposes


### Setup

App uses google project api key to make calls to the google places api for autocomplete and gecodings services. api key must be provided in a `test_api_keys.properties` file in the root directory like so:

```test_api_keys.properties
loki_test_google_api_key=<API KEY HERE>
rzrpay_test_key_id=<API KEY HERE>
rzrpay_test_key_secret=<API KEY HERE>
```
[test key](https://www.protectedtext.com/lokiapikey)

---

#### Payment Todo: 
- [ ] Confirmation Implementation
- [ ] Refund tracking on confirmation failure
- [ ] mock, testing, staging and production entrypoints.
- [ ] Getting Started README
- [ ] Separate into standalone library, and test in-app

#### Location TODO : 
- [ ] README
- [ ] testing and production entry-points
- [ ] Separate into standalone library, and test in-app

#### Notification TODO :
- [ ] KMM Notification Service
- [ ] README
- [ ] testing and production entry-points
- [ ] Separate into standalone library, and test in-app
