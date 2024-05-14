import request from '@/utils/request'

export function listTeamsByPage(query) {
    return request({
        url: '/team/list/page',
        method: 'get',
        params:query
    })
}