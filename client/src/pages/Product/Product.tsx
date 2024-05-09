import {Suspense} from 'react';
import {Await, useLoaderData, useNavigate} from 'react-router-dom';
import {useDispatch} from 'react-redux';
import styles from './Product.module.css';
import { IProduct } from '../../interfaces/product.interface';
import {cartActions} from '../../store/cart.slice.ts';
import Headling from '../../components/Headling/Headling.tsx';
import Button from '../../components/Button/Button.tsx';
import {CURRENCY} from "../../helper/variables.ts";

export function Product() {
	const data = useLoaderData() as { data: IProduct };
	const navigate = useNavigate();
	const dispatch = useDispatch();

	const addToCart = (event: React.MouseEvent, id: number) => {
		event.preventDefault();
		dispatch(cartActions.add(id));
	};

	return <>
		<Suspense fallback={'Loading...'}>
			<Await resolve={data.data}>
				{({data}: { data: IProduct }) => (
					<>
						<div className={styles['wrapper']}>
							<button className={styles['back']} onClick={() => navigate('/')}>
								<img src="/back-arrow-icon.svg" alt="Back to menu"/>
							</button>
							<Headling className={styles['headling']}>{data.name}</Headling>
							<Button appearence='small' onClick={(e) => addToCart(e, data.id)}>Add to cart</Button>
						</div>
						<div className={styles['card']}>
							<div className={styles['image']} style={{backgroundImage: `url('${data.image}')`}}></div>
							<div className={styles['description']}>
								<div className={styles['head']}>
									<div className={styles['line']}>
										<div className={styles['text']}>Price</div>
										<div className={styles['value']}>{data.price}&nbsp;<span>{CURRENCY}</span></div>
									</div>
									<hr className={styles['hr']}/>
									<div className={styles['line']}>
										<div className={styles['text']}>Rating</div>
										<div className={styles['rating']}>
											{data.rating}&nbsp;
											<img src="/star-icon.svg" alt="Star icon"/>
										</div>
									</div>
								</div>
								<div className={styles['footer']}>
									<div className={styles['line']}><span>Description:</span></div>
									<span className='list'>
										{data.description}
									</span>
								</div>
							</div>
						</div>
					</>
				)}
			</Await>
		</Suspense>
	</>;
}