import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import styles from './Cart.module.css';
import {CURRENCY, DELIVERY_FEE, SERVER} from '../../helper/variables';
import { cartActions } from '../../store/cart.slice';
import {AppDispatch, RootState, store} from '../../store/store';
import { IProduct } from '../../interfaces/product.interface';
import Headling from '../../components/Headling/Headling';
import CartItem from '../../components/CartItem/CartItem';
import Button from '../../components/Button/Button';

export function Cart() {
	const [cartProducts, setCardProducts] = useState<IProduct[]>([]);
	const items = useSelector((s: RootState) => s.cart.items);
	const jwt = store.getState().user.jwt
	const dispatch = useDispatch<AppDispatch>();
	const navigate = useNavigate();

	const total = items.map(i => {
		const product = cartProducts.find(p => p.id === i.productId);
		if (!product) {
			return 0;
		}
		return i.count * product.price;
	}).reduce((acc, i) => acc += i, 0);


	const getItem = async (id: number) => {
		const { data } = await axios.get<IProduct>(`${SERVER}/product/${id}`, {
			headers: {
				Authorization: `Bearer ${jwt}`
			}
		});
		return data;
	};

	const loadAllItems = async () => {
		const res = await Promise.all(items.map(i => getItem(i.productId)));
		setCardProducts(res);
	};

	const checkout = async () => {
		if (items.length > 0) {
			await axios.post(`${SERVER}/order`, {
				products: items
			}, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			dispatch(cartActions.clean());
			navigate('/success');
		}
	};

	useEffect(() => {
		loadAllItems();
	}, [items]);

	return <>
		<Headling className={styles['headling']}>Cart</Headling>
		{items.map(item => {
			const product = cartProducts.find(p => p.id === item.productId);
			if (!product) {
				return;
			}
			return <CartItem key={product.id} count={item.count} {...product} />;
		})}
		<div className={styles['line']}>
			<div className={styles['text']}>Total price</div>
			<div className={styles['price']}>{total}&nbsp;<span>{CURRENCY}</span></div>
		</div>
		<hr className={styles['hr']} />
		<div className={styles['line']}>
			<div className={styles['text']}>Delivery</div>
			<div className={styles['price']}>{DELIVERY_FEE}&nbsp;<span>{CURRENCY}</span></div>
		</div>
		<hr className={styles['hr']} />
		<div className={styles['line']}>
			<div className={styles['text']}>Total <span className={styles['total-count']}>({items.length})</span></div>
			<div className={styles['price']}>{total + DELIVERY_FEE}&nbsp;<span>{CURRENCY}</span></div>
		</div>
		<div className={styles['checkout']}>
			<Button appearence="big" onClick={checkout}>checkout</Button>
		</div>
	</>;
}