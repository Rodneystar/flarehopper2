import * as React from "react";
import { render as _render } from 'react-dom';
// import ModeButton from './ModeButton.js';
import TimerList from './TimerList';


export default class TimerListContainer extends React.Component<any, any> {

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
