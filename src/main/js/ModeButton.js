const React = require('react');
const ReactDOM = require('react-dom');
// const ModeButton = require('./ModeButton')

export default class ModeButton extends React.Component {

	constructor(props) {
		super(props);
	
	}

	render() {
		return (
            <div className="modebutton"> {this.props.mode} </div>
		)
	}
}