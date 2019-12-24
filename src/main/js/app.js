import React from 'react';
import { render as _render } from 'react-dom';
import ModeSwitchContainer from './ModeSwitchContainer.js';
import TimerListContainer from './TimerListContainer.js';
import '../styles/app.scss'
import { api } from "./apiClient"
import { MODES } from './flareDomain'

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			mode: MODES.OFF,
			timers: [],
		};
		this.switchMode = this.switchMode.bind(this)
		this.getMode = this.getMode.bind(this)
		this.getTimers = this.getTimers.bind(this)
		this.deleteTimer = this.deleteTimer.bind(this)

	}

	componentDidMount() {
		this.getMode();
		this.getTimers();
	}

	getMode() {
		api.getCurrentMode().then( mode => this.setState({mode}) )
	}

	switchMode(newMode) {
		api.switchMode(newMode).then( mode => this.setState({mode}) )
	}

	getTimers() {
		api.getTimers().then( timers => this.setState({timers}) )
	}

	deleteTimer( idx ) {
		if(idx==null){
			console.log("no item selected")
			return
		}
		api.delTimer(idx).then( timers => this.setState({timers}) )
	}

	render() {
		return (
			<div id="mainApp">
				<ModeSwitchContainer currentMode={this.state.mode} switchMode={this.switchMode} />
				<TimerListContainer  timers={this.state.timers } deleteTimer={this.deleteTimer} />
			</div>
		)
	}
}

_render(
	<App />,
	document.getElementById('react')
)