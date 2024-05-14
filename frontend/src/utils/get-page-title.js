import defaultSettings from '@/settings'

const title = defaultSettings.title || '友缘相逢-后台'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
