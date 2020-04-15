process.env.CHROME_BIN = require('puppeteer').executablePath()

module.exports = function (config) {
  config.set({
    browsers: ['ChromeHeadless'],
    basePath: 'target',
    files: ['unit-test.js'],
    frameworks: ['cljs-test'],
    plugins: [
      'karma-cljs-test',
      'karma-chrome-launcher',
      'karma-junit-reporter'
    ],
    colors: true,
    logLevel: config.LOG_INFO,
    client: {
      args: ['shadow.test.karma.init'],
      singleRun: true
    },
    junitReporter: {
      outputDir: 'target/junit/karma',
      outputFile: undefined,
      suite: ''
    }
  })
}
