
import {getCurrentMode} from '../../../main/js/apiClient'
import axiosBase from 'axios';
import {MODES} from '../../../main/js/flareDomain'

const axios = axiosBase.create({
    baseURL:  "http://localhost:8080"
})

test( 'axios call working', function() {
    return axios.get("/mode")
        .then( res => {
            console.log(res.data.mode)
        })
})

test( 'axios put mode', function() {
    return axios.put("/mode/OFF")
        .then( res => {
            console.log(res.data.mode)
        })
})


test('test objects', function() {
    console.log(Object.keys(MODES))
    console.log(Object.values(MODES))
})

function getMode() {

    let data = {
        mode: "OFF"
    }
    return data.mode
}
test('testd objects', function() {
    let result = getMode();
    let state = {result}

    console.log(JSON.stringify(result))
})
