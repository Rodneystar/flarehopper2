import React from 'react';
import { render as _render } from 'react-dom';
import ModeSwitchContainer from './ModeSwitchContainer.js';
import '../styles/app.scss'


class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {name: "jonnyer"};
	}

	render() {
		return (
			<ModeSwitchContainer/>
		)
	}
}

_render(
	<App />,
	document.getElementById('react')
)