This a a dummy function for the lenneflow application. 

Input payload example
```json
{
    "callBackUrl" : "http://localhost:8100/api/qms/callback/1/1/1",
    "inputData": {
        "minValue": 5,
        "maxValue": 50
        }
}
```

The function processes the payload, creates a output payload and send it to the callback url via post request.

Output payload example
```json
{
    "runStatus" : "COMPLETED",
    "failureReason": "",
    "outputData": {
        "randomValue": 15
        }
}
