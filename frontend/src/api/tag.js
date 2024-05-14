import request from '@/utils/request'

export function listTagByPage(query) {
    return request({
        url: '/tag/list/page',
        method: 'get',
        params:query
    })
}