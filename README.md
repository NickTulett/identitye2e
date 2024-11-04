# identitye2e
SDET car exercise

Test Description:
Write a test automation suite which does following.
1. Reads given input file: car_input.txt
2. Extracts vehicle registration numbers based on pattern(s).
3. Each number extracted from input file is fed to any car valuation website for e.g. webuyanycar
(Perform vehicle details search car valuation page with random mileage details)
4. Compare the output returned by car valuation website with given car_output.txt
5. Highlight/fail the test for any mismatches. Showcase your skills so it’s easier to add more
input files in future. Utilise any JVM based language with browser automation tools. Use
design patterns where appropriate.


# Solution:

In the `carTest` directory there is a Playwright-JVM project that can be opened in IntelliJ Aqua

I took the approach of parsing the `car_input.txt` file to extract registration numbers and estimated values.

Then submitting the registration numbers and a mileage of 25,000 to **Autotrader** to determine the car details and market value.

Finally I compared the information from AutoTrader to the estimated value and the details of the car in `car_output.txt`

To see the test results, run `TestAutoTrader.java` in the `src/../utils/tests` directory.

It contains a single parameterized test that is run for each of the registration numbers found in `car_input.txt`


The key test results are as follows:
```
1. java.lang.AssertionError: AD58VNF is too old (2008) for AutoTrader valuation
2. java.lang.AssertionError: BW57BOW does not appear in the output file
3. org.opentest4j.AssertionFailedError: Autotrader details missing part of the model name: CR ==> expected: <true> but was: <false>
4. org.opentest4j.AssertionFailedError: Autotrader details missing part of the model name: SG18 ==> expected: <true> but was: <false>
```

## Observations on test results:
1. I also created a utility file to parse valuations from **carwow**. It could be used to get a valuation for AD58VNF 
but carwow does not allow many consecutive requests, so I stopped using it and switched to AutoTrader 
while building the framework.
2. This is clearly a typo in the input file.
3. Not sure why Skoda model names have to be so long.
4. This is a typo in the output file where the reg number has been added to the model name.


## Notes:

It works but probably needs a clean up for consistency across the utilities and tests.

Any number of other cars can be added to the input and output files and will be included in the test automatically.

I created accounts on both carwow and AutoTrader and set Playwright up to use already open tabs, 
rather than creating a new browser - so that I wouldn't have to deal with logging in and handling cookie
dialogs and other popups. In production I would use specific accounts for testing.

This means that to run the test, you need to be running **a single** instance of Chrome, started with a `--remote-debugging-port=9222` flag.

I wouldn't use this version in production as it has a dependency on openCSV, which is reporting vulnerabilities.
