import React from 'react';
import { render as _render } from 'react-dom';
import ModeSwitchContainer from './ModeSwitchContainer.js';
import '../styles/app.scss'
import { api } from "./apiClient"
import { MODES } from './flareDomain'

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {mode: MODES.OFF};
		this.switchMode = this.switchMode.bind(this)
	}

	componentDidMount() {
		api.getCurrentMode().then( mode => {
			this.setState({mode})
		})
	}

	switchMode(newMode) {
		api.switchMode(newMode)
			.then( mode => {
				this.setState({mode}) 
			})
	}

	render() {
		return (
			<ModeSwitchContainer currentMode={this.state.mode} switchMode={this.switchMode} />
		)
	}
}

_render(
	<App />,
	document.getElementById('react')
)