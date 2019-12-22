const React = require('react');
const ReactDOM = require('react-dom');
import { MODES } from './flareDomain'

// const ModeButton = require('./ModeButton')

export default class ModeButton extends React.Component {

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