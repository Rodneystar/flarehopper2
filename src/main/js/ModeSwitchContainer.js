import React from 'react';
import ReactDOM from 'react-dom';
import ModeButton from './ModeButton.js';

export default class ModeSwitchContainer extends React.Component {

	constructor(props) {
		super(props);
	
	}

	render() {
		return (
            <div id="modeswitchcontainer">
                <ModeButton mode="TIMED" />
                <ModeButton mode="ON" />
    			<ModeButton mode="OFF" />
            </div>
		)
	}
}
