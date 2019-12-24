import React from 'react';
import ReactDOM from 'react-dom';
// import ModeButton from './ModeButton.js';
import TimerList from './TimerList.js';


export default class TimerListContainer extends React.Component {

	constructor(props) {
		super(props);
	
	}

	render() {
		return (
            <div id="timerlistcontainer">
				<TimerList {...this.props} />
            </div>

		)
	}
}
