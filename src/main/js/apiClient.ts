import axiosBase from 'axios';
import {MODES, timerEvent} from './flareDomain';

const axios = axiosBase.create( {
    headers: {
        "Content-Type": "application/json"
    }
})

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
    getTimers: function() : Promise<timerEvent[]> {
        return axios.get("/timers")
            .then( res => res.data )
    },
    delTimer: function( index ) {
        return axios.delete(`/timers/${index}`)
            .then( res => this.getTimers() )
    },
    addTimer: function( startTime, duration ) {
        return axios.post(`/timers/add`,
             JSON.stringify({ startTime, duration: `PT${duration}M` }))
    }
}
