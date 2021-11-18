import React, { Component } from 'react';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';

import { KeyCode } from 'components/util/common';
import { ObjUtil, IDTracker } from 'components/util/common';
import { Validator } from 'components/util/validator';
import { fas, FAButton } from 'components/widget/fa'
import { DialogContext, showDialog } from "components/widget/layout";

import './stylesheet.scss';

interface WAutoCompleteProps {
  value: any, inputField?: string, descriptionField?: string, autofocus?: boolean, disable?: boolean, style?: any,
  validators?: Array<Validator>,
  onInputChange: (selectBean: null | any, oldVal: string, newVal: string) => void,
  onCreateNew?: (WAutoComplete: WAutoComplete) => void;
  filter: (exp: string, onChangeCallback: (options: Array<any>) => void) => void,
};
interface WAutoCompleteState {
  input: any, dropdown: boolean, options: Array<any>, currSelect: number,
  focus: boolean, validated: boolean
};
/**@deprecated */
export class WAutoComplete extends Component<WAutoCompleteProps, WAutoCompleteState> {
  autoCompleteInput: any;
  inputWidth: number = 200;
  errorMessage: null | string = null;
  maxShowItem: number = 10;

  constructor(props: WAutoCompleteProps) {
    super(props);
    let { value } = props;
    this.autoCompleteInput = React.createRef()
    this.onFocus = this.onFocus.bind(this);
    this.onFocusLost = this.onFocusLost.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onKeyDown = this.onKeyDown.bind(this);
    let validated = value ? true : false;
    this.state = {
      input: value ? value : '', dropdown: false, options: [], currSelect: -1,
      focus: false, validated: validated
    };
    if (!validated) this.runInitValidation(null, value, value);
  }

  componentWillReceiveProps(nextProps: WAutoCompleteProps) {
    this.autofocus(nextProps);
    let { value } = nextProps;
    if (value === undefined) value = ''
    let validated = value ? true : false;
    this.setState({ input: value, dropdown: false, options: [], currSelect: -1, validated: validated });
    if (!validated) this.runValidation(null, value, value);
  }

  componentDidMount() {
    this.inputWidth = this.autoCompleteInput.current.offsetWidth
  }

  autofocus(props: WAutoCompleteProps) {
    if (props.autofocus) {
      this.inputWidth = this.autoCompleteInput.current.offsetWidth
      this.autoCompleteInput.current.focus();
    }
  }

  onFocus(evt: any) {
    if (this.props.disable) return;
    evt.target.select();
    this.setState({ focus: true })
  }

  onFocusLost(_evt: any) {
    let thisUI = this;
    let callback = () => {
      if (!thisUI.state.validated) {
        thisUI.updateValue(null, '');
        thisUI.setState({ focus: false })
      } else {
        thisUI.setState({ focus: false, dropdown: false, currSelect: -1 })
      }
    }
    setTimeout(callback, 200);
  }

  runInitValidation(_bean: any, _oldVal: any, newVal: any) {
    const { validators } = this.props;
    if (!validators) return;
    try {
      for (let i = 0; i < validators.length; i++) {
        validators[i].validate(newVal);
      }
    } catch (err) {
      this.errorMessage = err.message;
    }
  }

  runValidation(_bean: any, _oldVal: any, newVal: any) {
    const { validators } = this.props;
    if (!validators) return true;
    try {
      for (let i = 0; i < validators.length; i++) {
        validators[i].validate(newVal);
      }
    } catch (err) {
      this.errorMessage = err.message;
      this.setState({ dropdown: false, input: '', currSelect: -1, validated: false });
      return false;
    }
    this.errorMessage = null;
    this.setState({ validated: true });
    return true;
  }

  updateValue(bean: null | any, newVal: any) {
    const { onInputChange } = this.props;
    let oldVal = this.state.input;
    if (newVal && newVal.trim) newVal = newVal.trim();
    let validate = this.runValidation(bean, oldVal, newVal);
    if (validate) {
      this.setState({ dropdown: false, input: newVal, currSelect: -1 });
      if (onInputChange) onInputChange(bean, oldVal, newVal);
    }
  }

  /**
   * 1. onKeyDown is called before onChange
   * 2. onChange won't be called for certain key such ENTER, ESC...
   */
  onKeyDown(evt: any) {
    const { onCreateNew } = this.props;
    let keyCode = evt.keyCode;
    let { options, currSelect } = this.state;

    if (keyCode === KeyCode.ARROW_UP) {
      if (currSelect - 1 >= 0) {
        this.setState({ currSelect: currSelect - 1 });
      }
    } else if (keyCode === KeyCode.ARROW_DOWN) {
      let max = options.length;
      if (max > this.maxShowItem) max = this.maxShowItem;
      if (currSelect + 1 < max) {
        this.setState({ currSelect: currSelect + 1 });
      } else if (currSelect + 1 == max && onCreateNew) {
        this.setState({ currSelect: currSelect + 1 });
      }
    } else if (keyCode === KeyCode.ESC) {
      this.setState({ dropdown: false, input: '', currSelect: -1, validated: false });
    } else if (keyCode === KeyCode.ENTER) {
      let WAutoComplete = this;
      let onSelectCallback = () => {
        WAutoComplete.onSelectOption(WAutoComplete.state.currSelect);
      }
      setTimeout(onSelectCallback, 200);
    }
  }

  onChange(e: any) {
    let value = e.target.value;
    let { filter } = this.props;
    this.setState({ input: value, validated: false });
    if (filter) {
      let onChangeCallback = (options: Array<any>) => {
        let currSelect = options.length > 0 ? 0 : -1;
        let state: any = { options: options, dropdown: true, currSelect: currSelect };
        this.setState(state);
      }
      filter(value, onChangeCallback);
    }
  }

  onSelectOption(idx: number) {
    let { options } = this.state;
    let { onCreateNew, inputField } = this.props;
    let max = options.length;
    if (max > this.maxShowItem) max = this.maxShowItem;
    if (idx == max && onCreateNew) {
      onCreateNew(this);
      this.setState({ input: '', options: [], dropdown: false });
      return;
    }
    let selectOpt = options[idx];
    let newVal = null;
    if (inputField) {
      newVal = selectOpt[inputField];
    } else {
      newVal = selectOpt;
    }
    this.updateValue(selectOpt, newVal);
  }

  primitiveOptionRender(options: Array<any>, _selIndex: number) {
    let optionHtml = [];
    let max = options.length;
    if (max > this.maxShowItem) max = this.maxShowItem;
    for (let i = 0; i < max; i++) {
      let className = i === this.state.currSelect ? 'option-selected' : 'option';
      let active = false;
      let { currSelect } = this.state;
      if (currSelect === i) active = true;
      optionHtml.push((
        <DropdownItem key={i} active={active}>
          <div key={i} className={className} onClick={() => this.onSelectOption(i)}>
            {options[i]}
          </div>
        </DropdownItem>
      ));
    }
    if (options.length > max) {
      optionHtml.push(<div key='more' className='more'>...</div>);
    }
    return optionHtml;
  }

  renderOptions(options: Array<any>, inputField: string, descriptionField: string, _selIndex: number) {
    let optionHtml = [];
    let max = options.length;
    let { currSelect } = this.state;
    if (max > this.maxShowItem) max = this.maxShowItem;
    for (let i = 0; i < max; i++) {
      let opt = options[i];
      let active = false;
      if (currSelect === i) active = true;
      optionHtml.push((
        <DropdownItem key={i} active={active}>
          <div className='d-flex justify-content-between' key={i} onClick={() => this.onSelectOption(i)}>
            <div>{opt[inputField]}</div>
            <div>{opt[descriptionField]}</div>
          </div>
        </DropdownItem>
      ));
    }
    if (options.length > max) {
      optionHtml.push(<div key='more' className='more'>...</div>);
    }
    const { onCreateNew } = this.props;
    if (onCreateNew) {
      optionHtml.push(
        <DropdownItem key={'new'} active={currSelect == max}>
          <div className='btn-link' onClick={(_evt) => this.onSelectOption(options.length)}>Create New</div>
        </DropdownItem>
      );
    }
    return optionHtml;
  }

  toggle() { }

  render() {
    let { dropdown, options, focus } = this.state;
    let { inputField, descriptionField, disable, autofocus } = this.props;

    let dropdownContent = null;
    if (dropdown) {
      let optionHtml = null;
      if (inputField && descriptionField) {
        optionHtml = this.renderOptions(options, inputField, descriptionField, 0);
      } else {
        optionHtml = this.primitiveOptionRender(options, 0);
      }
      dropdownContent = (
        <DropdownMenu style={{ minHeight: '20px', width: this.inputWidth - 5 }}>
          {optionHtml}
        </DropdownMenu>
      )
    }

    let displayValue = this.state.input;
    let classes = 'form-control';
    if (this.errorMessage && !focus) {
      displayValue = this.errorMessage;
      classes = classes + ' form-control-error';
    }
    let html = (
      <div className='flex-hbox'>
        <Dropdown className='w-100' isOpen={this.state.dropdown} toggle={this.toggle}>
          <DropdownToggle className={'w-100'} style={{ background: 'none', border: 'none' }}>
            <input className={classes} ref={this.autoCompleteInput} value={displayValue}
              readOnly={disable} autoComplete="off" type={'text'} autoFocus={autofocus}
              onKeyDown={this.onKeyDown} onChange={this.onChange} onFocus={this.onFocus} onBlur={this.onFocusLost} />
          </DropdownToggle>
          {dropdownContent}
        </Dropdown>
      </div>
    );
    return html;
  }
}

export interface BBAutoCompleteProps {
  options: Array<any>, bean: any, field: string,
  inputField?: string, descriptionField?: string, /*for complex input bean*/
  validators?: Array<Validator>,
  autofocus?: boolean,
  useSelectOption?: boolean,
  context?: any, disable?: boolean, style?: any, hideMoreInfo?: boolean;
  onInputChange?: (bean: any, field: string, selectOpt: any, oldVal: any, newVal: any) => void
};
/**@deprecated */
export class BBAutoComplete<T extends BBAutoCompleteProps> extends Component<T, {}> {
  dialogContext: DialogContext | null = null;

  filter(exp: string, onChangeCallback: (selOptions: Array<any>) => void): void {
    const { options } = this.props;
    let selOptions = this.doFilter(exp, options);
    onChangeCallback(selOptions);
  }

  doFilter(exp: string, records: Array<any>) {
    let selRecords = [];
    for (let i = 0; i < records.length; i++) {
      let record = records[i];
      if (ObjUtil.recordHasExpression(record, exp)) {
        selRecords.push(record);
      }
    }
    return selRecords;
  }

  onCreateNew?: (WAutoComplete: WAutoComplete) => void;

  getContext() { return this.props.context; }

  onInputChange(selectOpt: null | any, oldVal: any, newVal: any) {
    const { bean, field, onInputChange, useSelectOption } = this.props;
    if (onInputChange) {
      onInputChange(bean, field, selectOpt, oldVal, newVal);
    } else {
      if (useSelectOption) {
        if (!bean[field]) bean[field] = selectOpt;
        else ObjUtil.replaceProperties(bean[field], selectOpt);
      } else {
        bean[field] = newVal;
      }
      this.forceUpdate();
    }
  }

  onShowMoreInfo(value: any) {
    let ui = (<pre style={{ height: 500 }}>Custom More Info {JSON.stringify(value, null, '  ')}</pre>)
    this.dialogShow('More Info', 'md', ui);
  }

  onCustomSelect() {
    let ui = (<div style={{ height: 300 }}>Custom Select</div>)
    this.dialogShow('Custom Select', 'md', ui);
  }

  dialogShow(title: string, size: 'xs' | 'sm' | 'md' | 'lg' | 'xl', ui: any) {
    this.dialogContext = new DialogContext();
    showDialog(title, size, ui, this.dialogContext);
  }

  dialogClose() {
    if (this.dialogContext) {
      this.dialogContext.getDialog().doClose();
      this.dialogContext = null;
    }
  }

  render() {
    const { bean, field, inputField, descriptionField, validators, disable, style, autofocus, useSelectOption } = this.props;
    let onCreateNew = undefined;
    if (this.onCreateNew) onCreateNew = this.onCreateNew
    let inputValue: any = null;
    if (useSelectOption) {
      let nestedBean: any = bean[field];
      if (nestedBean) {
        inputValue = nestedBean[inputField];
      }
    } else {
      inputValue = bean[field];
    }
    let html = (
      <div className='d-flex'>
        <WAutoComplete key={IDTracker.next()} style={style} value={inputValue} autofocus={autofocus}
          inputField={inputField} descriptionField={descriptionField} validators={validators} disable={disable}
          filter={(val, onChangeCallback) => this.filter(val, onChangeCallback)}
          onInputChange={(selectBean, oldVal, newVal) => this.onInputChange(selectBean, oldVal, newVal)} onCreateNew={onCreateNew} />
        {this.renderControl()}
      </div>
    );
    return html;
  }

  renderControl() {
    const { bean, disable, hideMoreInfo } = this.props;
    if (disable) return null;
    let moreInfoBtn = null;
    if (!hideMoreInfo) {
      moreInfoBtn = (<FAButton color='link' icon={fas.faInfo} onClick={() => this.onShowMoreInfo(bean)} />);
    }
    let html = (
      <div className="d-flex flex-grow-0">
        {moreInfoBtn}
        <FAButton color='link' icon={fas.faSearch} onClick={() => this.onCustomSelect()} />
      </div>
    );
    return html;
  }
}