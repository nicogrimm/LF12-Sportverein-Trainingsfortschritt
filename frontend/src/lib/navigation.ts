export type Page = 'test1' | 'test2';

export function switchPage(page: Page) {
  // basic logic for now
  window.location.pathname = '/' + page;
}
