import React from "react"
import update from "react-addons-update"

import Udidb from "./udidb.js"
import { UdidbRequest, PUT_METHOD, POST_METHOD } from "./requests.js"
import staticProps from "./staticData.js"

export default React.createClass({

    getInitialState: function() {
        return staticProps;
    },

    render: function() {
        return (
            <Udidb {...this.state} process={this.process}/>
        );
    },

    process: function(request) {
        switch (request.getMethod()) {
            case POST_METHOD:
                this._processPost(request);
                break;
            case PUT_METHOD:
                this._processPut(request);
                break;
            default:
                console.log("Request with unknown method: " + request);
                break;
        }
    },

    _processPost: function(request) {
        switch (request.getPath()) {
            case "currentContext.operation":
                this._newOperation(request.getValue());
                break;
            default:
                console.log("POST with unknown path: " + request);
                break;
        }
    },

    _processPut: function(request) {
        switch (request.getPath()) {
            case "currentContextIndex":
                this._selectContext(request.getValue());
                break;
            case "currentContext.activeThreadIndex":
                this._selectActiveThread(request.getValue());
                break;
            default:
                console.log("PUT with unknown path: " + request);
                break;
        }
    },

    _newOperation: function(value) {
        let fields = value.split(" ");
        let newState = update(this.state, {
            history: { 
                operations: {
                    $push: [{ 
                        name: fields[0], 
                        operands: [ {
                            name: "value",
                            type: "list",
                            value: fields.slice(1),
                        }],
                        result: null
                    }]
                }
            }
        });
        this.setState(newState);
    },

    _selectContext: function(index) {
        let newState = update(this.state, {
            currentContextIndex: { $set: index }
        });
        this.setState(newState);
    },

    _selectActiveThread: function(index) {
        let currentContextIndex = this.state.currentContextIndex;
        let contextUpdate = { };
        contextUpdate[currentContextIndex] = { activeThreadIndex: { $set: index } };
        let newState = update(this.state, {
            contexts: contextUpdate
        });
        console.log(JSON.stringify(newState.contexts));
        this.setState(newState);
        console.log(JSON.stringify(this.state.contexts));
    },
});