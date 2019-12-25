import * as React from "react";
import { render as _render } from 'react-dom';

// const ModeButton = require('./ModeButton')

export default class ModeButton extends React.Component<any, any> {

	constructor(props) {
		super(props);
	
	}

	render() {
		const {active, switchMode, mode} = this.props;
		let classN = active ? "activemodebutton": "inactivemodebutton"
		let handleClick = function(event){ switchMode(mode) }
		return (
            <div className={classN} onClick={handleClick}> {this.props.mode} </div>
		)
	}
}