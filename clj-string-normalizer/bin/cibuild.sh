#!/usr/bin/env bash

COMMAND=$1

PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. >/dev/null 2>&1 && pwd )"
cd $PROJECT_DIR

npm_install() {
  npm install
}

lint() {
  npm run lint
}

test() {
  lein test:clj
  lein test:cljs
}

deploy() {
  lein do clean, compile, deploy
}

case $COMMAND in
  "npm:install" )
    npm_install
    ;;
  "lint" )
    lint
    ;;
  "test" )
    test
    ;;
  "deploy" )
    deploy
    ;;
esac
