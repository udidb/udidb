var initialState = {
    contexts: [],

    currentContextIndex: -1,

    prefs: {
        history: {
            numDisplayedOps: 3,
        },
    },

    defaultSourceContent: [
        " ",
        "udidb - UDI debugger",
        " ",
        " ",
        " ",
        " ",
        "Create processes with the 'create' operation.",
        " ",
        "Enter 'help' at the prompt for a list of available operations.",
        " ",
        " ",
        " ",
        " ",
        " ",
        " ",
        " ",
        " ",
        " ",
        " ",
    ],

    globalContext: {
        history: {
            baseIndex: 0,
            operations: []
        },
        operationDescriptors: []
    },

    modal: {
        show: false,
        text: "",
        body: ""
    },
};

export default initialState;
