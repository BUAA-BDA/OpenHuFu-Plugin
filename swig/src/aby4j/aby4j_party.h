#ifndef __ABY4J_PARTY_H__
#define __ABY4J_PARTY_H__


#include <abycore/circuit/booleancircuits.h>
#include <abycore/circuit/arithmeticcircuits.h>
#include <abycore/circuit/circuit.h>
#include <abycore/sharing/sharing.h>
#include <abycore/aby/abyparty.h>
#include <memory>
#include <map>
#include <fstream>

namespace aby4j {
  using std::unique_ptr;
  using std::shared_ptr;

  class Party {
  public:
    Party(uint32_t party_id, const std::string& server_addr, uint16_t server_port);

    bool GreaterI32(e_role role, int32_t val1, int32_t val2, bool shared=false);

    int MaxI32(e_role role, int32_t val1, int32_t val2, bool shared=false);

    int MinI32(e_role role, int32_t val1, int32_t val2, bool shared=false);
    
  private:
    ABYParty* party_;
  };
}

#endif //__ABY4J_PARTY_H__