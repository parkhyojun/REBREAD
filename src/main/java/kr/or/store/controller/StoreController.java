package kr.or.store.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import kr.or.member.model.vo.Member;
import kr.or.order.model.vo.Order;
import kr.or.store.model.service.StoreService;
import kr.or.store.model.vo.Store;
import kr.or.store.model.vo.StoreDetail;
import kr.or.store.model.vo.StorePageData;
import kr.or.product.model.vo.Product;

@Controller
public class StoreController {

	@Autowired
	private StoreService sservice;
	
	@ResponseBody
	@RequestMapping(value="/storeUpdate.do", produces = "application/json;charset=utf-8")
	public void storeUpdate(Store s) {
		int result = sservice.updateStoreDetail(s);
	}
	

	// ceoStoreInfo 이동 (가게 정보)
	@RequestMapping(value="/ceoStoreInfo.do")
	public String ceoStoreInfo(Model model, HttpSession session){	
		Member member = (Member)session.getAttribute("m");
//		System.out.println(member.getMemberNo());
		ArrayList<Store> list = sservice.selectMemberStore(member);
		model.addAttribute("list",list);
		return "store/ceoStoreInfo";
	}
	
	@RequestMapping(value = "/storeInsert.do")
	public String storeInsert(Store s) {
//		ArrayList<StoreFileVO> storeFileList = new ArrayList<StoreFileVO>();
//		if(!boardFile[0].isEmpty()) {
//			String savePath = request.getSession().getServletContext().getRealPath("/resources/upload/board/");
//			for(MultipartFile file : boardFile) {
//				String filename = file.getOriginalFilename();
//				String filepath = fileRename.fileRename(savePath, filename);
//				
//				FileOutputStream fos;
//				try {
//					fos = new FileOutputStream(new File(savePath+filepath));
//					BufferedOutputStream bos = new BufferedOutputStream(fos);
//					byte[] bytes = file.getBytes();
//					bos.write(bytes);
//					bos.close();
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				// 파일 업로드 끝(파일 1개 업로드)
//				FileVO fv = new FileVO();
//				fv.setFilename(filename);
//				fv.setFilepath(filepath);
//				fileList.add(fv);
//			}
//		}
//		b.setFileList(fileList);
		
		int result = sservice.insertStore(s);
		if(result>0) {
			return "redirect:/";
		}else {
			return "store/ceoStoreInfo";
		}
	}
	
	// 가게 정보 페이징 처리
//	@RequestMapping(value = "/storeInfoList.do")
//	public String storeInfoList(int reqPage, Model model) {
//		StorePageData spd = sservice.selectStoreList(reqPage);
//		model.addAttribute("list",spd.getList());
//		model.addAttribute("pageNavi",spd.getPageNavi());
//		model.addAttribute("reqPage",spd.getReqPage());
//		model.addAttribute("numPerPage",spd.getNumPerPage());
//		return "store/ceoStoreInfo";
//	}
		
	// 가게 정보 수정창으로 이동
	@RequestMapping(value = "/storeInfoUpdate.do")
	public String storeInfoUpdate(Store s, Model model) {
		Store store = sservice.selectOneStore1(s);
		model.addAttribute("s",store);
		return "store/storeInfoUpdate";
	}
	
	@RequestMapping(value = "/storeInfoUpdateSuccess.do")
	public String storeInfoUpdateSuccess(Store s) {
		Store store = sservice.updateStore(s);
		return "redirect:/ceoStoreInfo.do";
	}
	
	
	// ceoStoreSalesInfo 이동 (판매 정보 관리)
	@RequestMapping(value="/ceoStoreSalesInfo.do")
	public String ceoStoreSalesInfo(Model model) {
		ArrayList<Order> list = sservice.selectAllOrder();
		model.addAttribute("list", list);
		return "/store/ceoStoreSalesInfo";
	}
	
	// 선택한 배송 상태에 따라 정보 출력
	@RequestMapping(value = "/salesInfoSelect.do")
	public String salesInfoSelect(Order o, Model model) {
		ArrayList<Order> list = sservice.selectWhereOrder(o);
		model.addAttribute("list", list);
		return "/store/ceoStoreSalesInfo";
	}
	
	// 상품 배송 상태 변경
	@RequestMapping(value = "/salesInfoUpdate.do")
	public String salesInfoUpdate(Order o) {
		int result = sservice.salesInfoUpdate(o);
		if(result>0) {
			o.setOrderNo(o.getOrderNo());
			o.setOrderState(o.getOrderState());
		}
		return "redirect:/ceoStoreSalesInfo.do";
	}
	// 매장 리스트 출력
	@RequestMapping(value="/allStoreList.do")
	public String allStoreList(int reqPage,Model model,String storeName) {
		StorePageData spd = sservice.selectStoreList(reqPage,storeName);
		//System.out.println(spd);
		model.addAttribute("list",spd.getList());
		model.addAttribute("pageNavi",spd.getPageNavi());
		model.addAttribute("reqPage",spd.getReqPage());
		model.addAttribute("numPerPage", spd.getNumPerPage());
		model.addAttribute("storeName",storeName);
		return "store/storeList";
	}
	//매장 상세페이지
	@RequestMapping(value="/detailStore.do")
	public String detailStore(int storeNo, Model model) {
		//Store s = sservice.selectOneStore2(storeNo);
		//System.out.println(s);
		//model.addAttribute("s",s);
		StoreDetail sd = sservice.selectOneStore2(storeNo);
		//System.out.println(sd);
		model.addAttribute("sd",sd);
		return "store/detailStore";
	}
	@ResponseBody 
	@RequestMapping(value="/storeSearch.do", produces = "application/json;charset=utf-8")
	public String storeSearch(String storeName) {
		ArrayList<Store> list = sservice.searchStore(storeName);
		return new Gson().toJson(list);
	}
	
	//스토어리스트
	@RequestMapping(value = "/purchaseList.do")
	public String purchaseList(int reqPage,Model model,String storeName) {
		StorePageData spd = sservice.selectStoreList(reqPage,storeName);
		model.addAttribute("list",spd.getList());
		model.addAttribute("pageNavi",spd.getPageNavi());
		model.addAttribute("reqPage",spd.getReqPage());
		model.addAttribute("numPerPage", spd.getNumPerPage());
		model.addAttribute("storeName",storeName);
		return "store/purchaseList";
	}
	//장바구니 데이터 결제페이지로 이동
	@RequestMapping(value="/orderFrm.do")
	public String orderFrm(int storeNo,int memberNo, String deliveryType,int[] pNo, String[] pName,String[] pContent,
			int[] pStock, int[] pPrice, String[] pImg, Model model) {
		
		//System.out.println(pNo[0]);	
		ArrayList<Product> list = new ArrayList<Product>();
		for(int i=0;i<pName.length;i++) {
			Product p = new Product();
			p.setProductNo(pNo[i]);
			p.setProductName(pName[i]);
			p.setProductContent(pContent[i]);
			p.setProductStock(pStock[i]);
			p.setProductPrice(pPrice[i]);
			p.setProductImg(pImg[i]);
			list.add(p);
		}
		model.addAttribute("list",list);
		model.addAttribute("storeNo",storeNo);
		model.addAttribute("memberNo",memberNo);
		model.addAttribute("type",deliveryType);
		return "order/order";
	}
	
}

