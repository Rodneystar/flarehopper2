import * as React from "react";
import { render as _render } from 'react-dom';
import ModeSwitchContainer from './ModeSwitchContainer';
import TimerListContainer from './TimerListContainer';
import '../styles/app.scss';
import { api } from "./apiClient";
import { MODES, timerEvent } from './flareDomain';

interface State {
	mode: string;
	timers: timerEvent[];
}

class App extends React.Component< any , State> {

	constructor(props) {
		super(props);
		this.state = {
			mode: MODES.OFF,
			timers: []
		};
		this.switchMode = this.switchMode.bind(this)
		this.getMode = this.getMode.bind(this)
		this.getTimers = this.getTimers.bind(this)
		this.deleteTimer = this.deleteTimer.bind(this)
		this.addTimer = this.addTimer.bind(this)
	}

	componentDidMount() {
		this.getMode();
		this.getTimers();
	}

	getMode() {
		api.getCurrentMode().then( mode => super.setState({mode}) )
	}

	switchMode(newMode) {
		api.switchMode(newMode).then( mode => this.setState({mode}) )
	}

	getTimers() {
		api.getTimers().then( (timers: timerEvent[]) => this.setState({timers}) )
	}

	deleteTimer( idx ) {
		if(idx==null){
			console.log("no item selected")
			return
		}
		api.delTimer(idx).then( timers => this.setState({timers}) )
	}

	addTimer( startTime, duration) {
		api.addTimer( startTime, duration ).then( (res) => this.getTimers() )
	}

	render() {
		return (
			<div id="mainApp">
				<ModeSwitchContainer currentMode={this.state.mode} switchMode={this.switchMode} />
				<TimerListContainer  timers={this.state.timers } deleteTimer={this.deleteTimer} addTimer={ this.addTimer } />
			</div>
		)
	}
}

_render(
	<App />,
	document.getElementById('react')
)