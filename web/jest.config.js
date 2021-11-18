module.exports = {
  "moduleDirectories": [
    "node_modules",
    "src",
    "tests",
  ],
  "moduleNameMapper": {
    "\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$": "<rootDir>/__mocks__/fileMock.js",
    "\\.(css|less|scss|sass)$": "identity-obj-proxy"
  },
  "transform": {
    "^.+\\.tsx?$": "ts-jest"
  },
  "testRegex": "(/__tests__/.*|(\\.|/)(test|spec))\\.(jsx?|tsx?)$",
  "moduleFileExtensions": [ "ts", "tsx", "js", "jsx", "json", "node" ],
  "setupFiles": [
    "<rootDir>/tests/setupTests.ts"
  ]
};
