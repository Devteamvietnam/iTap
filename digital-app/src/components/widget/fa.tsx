import React, { Component } from "react";

import { IconDefinition, SizeProp } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon, FontAwesomeIconProps } from '@fortawesome/react-fontawesome'
import * as fas from '@fortawesome/free-solid-svg-icons'
import * as far from '@fortawesome/free-regular-svg-icons'
import * as brands from '@fortawesome/free-brands-svg-icons'

import { Button, ButtonProps } from 'reactstrap';

export { fas, far, brands }
export { FontAwesomeIcon as FAIcon }
export { IconDefinition as FAIconDefinition }

export class FALabel extends Component<FontAwesomeIconProps> {
  render() {
    let { children } = this.props;
    return (<span><FontAwesomeIcon {...this.props} /> {children}</span>);
  }
}

export interface FAButtonProps extends ButtonProps {
  icon?: IconDefinition;
  iconSize?: SizeProp;
  iconInverse?: boolean;
  hint?: string;
}
export class FAButton extends Component<FAButtonProps> {
  render() {
    if(this.props.hidden) return null;

    let { className, style, size, onClick, color, outline, hint, children, disabled, id } = this.props;
    let { icon, iconSize, iconInverse } = this.props;
    let iconUI = null;
    if (!color) color = 'primary';
    if (!size) size = "sm";
    if (icon) {
      iconUI = (
        <FontAwesomeIcon size={iconSize ? iconSize : '1x'} inverse={iconInverse} icon={icon} />
      );
    }
    return (
      <Button id={id} className={className} style={style} disabled={disabled}
        size={size} color={color} outline={outline} onClick={onClick} title={hint}>
        {iconUI} {children}
      </Button>
    );
  }
}