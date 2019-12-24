import axios from 'axios';
import {MODES} from './flareDomain';

function validateNewMode(newMode) {
    if(!Object.values(MODES).includes(newMode)) {
        throw "invalid new mode";
    }
}
export const api = {
    getCurrentMode: function() {
        return axios.get("/mode" )
            .then( res => res.data.mode)
    },
    switchMode: function(newMode) {
        validateNewMode(newMode)
        return axios.put(`/mode/${newMode}`)
            .then( res => res.data.mode)
    },
    getTimers: function() {
        return axios.get("/timers")
            .then( res => res.data )
    }
}
