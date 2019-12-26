import * as React from "react";
import { render as _render } from 'react-dom';
// import ModeButton from './ModeButton.js';
import TimerList from './TimerList';
import AddTimer from './AddTimer';
import { timerEvent } from "./flareDomain";

interface Props {
	timers : timerEvent[];
	deleteTimer: (index: Number) => void;
}

export default class TimerListContainer extends React.Component<Props, any> {

	constructor(props) {
		super(props);
	
	}

	render() {
		return (
            <div id="timerlistcontainer">
				<TimerList {...this.props} />
				<AddTimer />
            </div>

		)
	}
}
