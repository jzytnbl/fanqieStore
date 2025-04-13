import {axios} from '../utils/request'
import {CART_MODULE} from "@/api/_prefix";

type CartInfo = {
    productId: string,
    title: string,
    price: number,
    description: string,
    cover: string,
    detail: string,
    quantity: number,
}

// 添加商品到购物车
export const addBookToCart = (cartInfo : CartInfo) => {
    return axios.post(`${CART_MODULE}`, cartInfo, {headers:{'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}

// 删除购物车商品
export const deleteCart = (cartItemId: string) => {
    return axios.delete(`${CART_MODULE}/${cartItemId}`).then(res => {
        return res
    })
}

// 修改购物车商品数量
export const getProduct = (cartItemId: string, quantity: number) => {
    return axios.patch(`${CART_MODULE}/${cartItemId}`, {
        quantity: quantity,
    }).then(res => {
        return res
    })
}

// 获取购物车商品列表
export const getCart = () => {
    return axios.get(`${CART_MODULE}`).then(res => {
        return res
    })
}