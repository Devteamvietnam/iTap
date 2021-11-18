import React from 'react'
import { app, widget, reactstrap } from 'components';

import {
  WComponentProps, WComponent
} from 'core/widget'
import { popupUIPreview } from './utilities';

const { FAButton } = widget.fa;

type ThumbnailCreator = (ctx: app.AppContext, label: string, ext: string, url: string) => any;

let DefaultThumbnailCreator = (_ctx: app.AppContext, _label: string, ext: string, _url: string) => {
  let text = ext.toUpperCase();
  let html = (
    <div className='flex-vbox align-middle text-center pt-2'
      style={{ fontSize: '2.75em', textShadow: '2px 2px 4px #000' }}>
      {text}
    </div>
  );
  return html;
}

let ImageThumbnailCreator = (ctx: app.AppContext, _label: string, _ext: string, url: string) => {
  url = ctx.getServerContext().createServerURL(`/storage/${url}`, {});
  let html = (
    <div className='flex-hbox justify-content-center'>
      <img src={url} height="70" />
    </div>
  );
  return html;
}
class ThumbnailFactory {
  thumbnails: Record<string, ThumbnailCreator> = {};

  constructor() {
    this.thumbnails['default'] = DefaultThumbnailCreator;
    this.thumbnails['png'] = ImageThumbnailCreator;
    this.thumbnails['jpg'] = ImageThumbnailCreator;
  }

  create(ctx: app.AppContext, label: string, url: string) {
    if (!url) url = label;
    let idx = url.lastIndexOf('.');
    let ext = 'Unknown';
    if (idx > 0) {
      ext = url.substr(idx + 1);
    }
    let creator = this.thumbnails[ext];
    if (!creator) creator = this.thumbnails['default'];
    return creator(ctx, label, ext, url);
  }
}

const FACTORY = new ThumbnailFactory();

interface WPreviewThumbnailProps extends WComponentProps {
  label: string;
  url: string;
  params?: any
}
export class WPreviewThumbnail extends WComponent<WPreviewThumbnailProps> {
  onPreview() {
    let { url, params } = this.props;
    popupUIPreview(this.props, url, params);
  }

  render() {
    let { appContext, label, url } = this.props;
    let html = (
      <div className='flex-vbox border m-1'>
        <div className='flex-vbox'>{FACTORY.create(appContext, label, url)}</div>
        <div className='flex-hbox-grow-0 justify-content-center border-top'>
          <FAButton color='link' onClick={() => this.onPreview()}>{label}</FAButton>
        </div>
      </div>
    );
    return html;
  }
}

interface UIPreviewProps extends WComponentProps {
  url: string;
  params?: any
}
export class UIPreview extends WComponent<UIPreviewProps> {
  render() {
    let { appContext, url, params } = this.props;
    url = appContext.getServerContext().createServerURL(`/storage/${url}`, params);

    let { Button } = reactstrap;
    let html = (
      <div className='flex-vbox'>
        <object className='flex-vbox' data={url} />
        <div className='d-flex justify-content-end' style={{ height: 35 }}>
          <Button className='mx-1' color="secondary" onClick={() => console.log('to do...')}>Download</Button>
        </div>
      </div>
    );
    return html;
  }
}